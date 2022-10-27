package me.edoardo.pieces;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class MerkleSingleton {
	// utility function: returns the HSA-256 of byte[]
	public byte[] hashOf(byte[] sbytes) throws NoSuchAlgorithmException {
		// TODO: MessageDigest must be passed
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] dd = digest.digest(sbytes);
		// System.out.println("just " + hex(dd));

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
			System.out.println("level has " + nodes.size() + " nodes...");

			// Successively pair up nodes at each level 
			for (int i = 0; i < nodes.size(); i += 2) {
				// Create the parent node, which we will add a left, a right, then calculate the hash for the node
				Node levelParent = new Node();
				parents.add(levelParent);
				// Assign the left, which will always be there
				levelParent.left = nodes.get(i);
				levelParent.left.parent = levelParent; // pointers to allow navigation from leafs 	
				if (nodes.size() == (i + 1)) {
					// if we are at the far right of the three
					// there is no right node
				} else {
					levelParent.right = nodes.get(i + 1);
					levelParent.right.parent = levelParent;
				}

				// compute the node hash
				levelParent.hash = levelParent.treeHash();

			}

			// Once all pairs have been made, the parents now become the 
			// children and we start all over again
			nodes = parents;
		}

		// Return the single node as our root
		return nodes.get(0);

		// ArrayList<MerkleTree> parents = new ArrayList<MerkleTree>();

		// int remDataLength = itemData.length;
		// int half =  remDataLength / 2; 
		// int rem = remDataLength % 2; 

		// byte[] leftPart = Arrays.copyOfRange(itemData, 0, half);
		// byte[] rightPart = Arrays.copyOfRange(itemData, (half + rem), remDataLength);

		// System.out.println("bytes for the left subtree " + half); // 4290
		// System.out.println("bytes for the right subtree " + (half + rem)); // 4291
		// System.out.println("bytes for the left subtree " + leftPart.length); // 4290

		// Node mkt = new Node();

		// mkt.left = build(itemData)

		// return mkt;
	}
	// this is to get the sibling of a given node
	static Node sibling(Node nn) {
		if (nn.parent != null) { // I am not in the root
			if (nn == nn.parent.left) {
				return nn.parent.right;
			} else
				return nn.parent.right;
		} else
			return null;
	}

	static void browse(Node nn) {
		if (nn.parent != null) { // I am not in the root
			System.out.println("item " + hex(sibling(nn).hash));
			browse(nn.parent);
		}
	}

	private static MerkleSingleton singleton = null;

	private MerkleSingleton(String which) throws NoSuchAlgorithmException, IOException {
        System.out.println("INIT: ");
		byte[] itemData = Files.readAllBytes(Paths.get(which));

		ArrayList<Node> leafNodes = new ArrayList<Node>();
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

		Node rootNode = build(leafNodes);
		System.out.println("ROOT should be 9b39e1edb4858f7a3424d5a3d0c4579332640e58e101c29f99314a12329fc60b: " + hex(rootNode.hash));
		Node item = leafNodes.get(8);
		System.out.println("CONTENT: " + new String(Base64.getEncoder().encode(item.chunk)));
		browse(item);		
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
