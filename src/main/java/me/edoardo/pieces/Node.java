package me.edoardo.pieces;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Node {
	public Node parent;
	public Node left;
	public Node right;
	public byte[] hash;
	public byte[] chunk;

	// utility function: not very readable but efficient way to add two arrays in java
	public byte[] add(byte[] first, byte[] second) {
		byte[] both = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, both, first.length, second.length);
		return both;
	}

	// utility function: returns the HSA-256 of byte[]
	public byte[] hashOf(byte[] sbytes) throws NoSuchAlgorithmException {
		// TODO: MessageDigest must be passed
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] dd = digest.digest(sbytes);
		// System.out.println("just " + hex(dd));

		return dd;
	}

	/** calculates the SHA256 of [left, right] 
	 * @throws NoSuchAlgorithmException
	 */
	public byte[] treeHash() throws NoSuchAlgorithmException {
		byte[] leftHash = this.left.hash;
		byte[] rightHash = this.right != null ? this.right.hash : leftHash; // If no right child exists, reuse the left child's value
		return hashOf(add(leftHash, rightHash));
	}

	/** constructor that calculates the SHA256 of this leaf data chunk
	 * @throws NoSuchAlgorithmException
	 */
	public Node(byte[] chunk) throws NoSuchAlgorithmException {
		this.chunk = chunk;
		this.hash = hashOf(chunk);
	}

	public Node() {
	}
}