package me.edoardo.pieces;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MerkleSingleton {
	// utility function: returns the HSA-256 of byte[]
	public byte[] hashOf(byte[] sbytes) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] dd = digest.digest(sbytes);
		return dd;
	}

	// utility function: convert byte[] to a hexadecimal representation
	public static String hex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte aByte : bytes) {
			result.append(String.format("%02x", aByte));
		}
		return result.toString();
	}

	static Node build(ArrayList<Node> nodes) throws NoSuchAlgorithmException {
		// Loop until we reach a single node, which will be our Merkle root
		while (nodes.size() > 1) {
			ArrayList<Node> parents = new ArrayList<Node>();
			System.out.println("--------------------------------------------");
			System.out.println("level has " + nodes.size() + " nodes...");

			// Successively pair up nodes at each level 
			for (int i = 0; i < nodes.size(); i += 2) {
				// Create the parent node, which we will add a left, a right, then calculate the hash for the node
				Node levelParent = new Node();
				parents.add(levelParent);
				// Assign the left, which will always be there
				levelParent.left = nodes.get(i);
				levelParent.left.parent = levelParent; // pointer to allow navigation from leafs 	
				if (nodes.size() == (i + 1)) {
					levelParent.right = nodes.get(i);
					// if we are at the far right of the tree, there is no right node, so I put itself
				} else {
					levelParent.right = nodes.get(i + 1);
					levelParent.right.parent = levelParent;
				}
				levelParent.hash = levelParent.treeHash(); // compute the node hash
			}
			// parents now become the children and we restart 
			nodes = parents;
		}
		// Return the single node as our root
		return nodes.get(0);
	}

	// this is to get the sibling of a given node
	static Node sibling(Node nn) {
		if (nn == nn.parent.left) {
			return nn.parent.right;
		} else
			return nn.parent.left;
	}

	static void browse(Node nn, ArrayList<String> out) {
		if (nn.parent != null) { // I am not in the root
			out.add(hex(sibling(nn).hash));
			browse(nn.parent, out);
		}
	}

	public String rootAsString() {
		return hex(rootNode.hash);
	}

	public int numPieces() {
		return leafNodes.size();
	}

	private static MerkleSingleton singleton = null;
	public Node rootNode;
	ArrayList<Node> leafNodes;

	public Map<String, Object> proofResponse(int whichLeaf) {
		HashMap<String, Object> proof = new HashMap<>();
		Node item = leafNodes.get(whichLeaf);
		ArrayList<String> inheritance = new ArrayList<String>();
		browse(item, inheritance);
		proof.put("proof", inheritance);
		proof.put("content", new String(Base64.getEncoder().encode(item.chunk)));
		return proof;
	}

	private MerkleSingleton(String which) throws NoSuchAlgorithmException, IOException {
		System.out.println("INIT: ");
		byte[] itemData = Files.readAllBytes(Paths.get(which));

		leafNodes = new ArrayList<Node>();
		for (int i = 0; i < itemData.length; i += 1024) {
			byte[] chunk;
			if (i + 1024 < itemData.length) { // we have a full chunk
				chunk = Arrays.copyOfRange(itemData, i, i + 1024);
			} else {
				chunk = Arrays.copyOfRange(itemData, i, itemData.length);
				chunk = Arrays.copyOf(chunk, 1024); // is the last one, pad with 0s
			}
			Node nth = new Node(chunk);
			leafNodes.add(nth);
		}
		System.out.println("created " + leafNodes.size() + " leafs...");
		rootNode = build(leafNodes);
	}

	public static MerkleSingleton getInstance() {
		if (singleton == null) {
			throw new AssertionError("You have to call init first");
		}

		return singleton;
	}

	public synchronized static MerkleSingleton init(String which) throws NoSuchAlgorithmException, IOException {
		if (singleton != null) {
			throw new AssertionError("You already initialized me");
		}
		singleton = new MerkleSingleton(which);
		return singleton;
	}

}
