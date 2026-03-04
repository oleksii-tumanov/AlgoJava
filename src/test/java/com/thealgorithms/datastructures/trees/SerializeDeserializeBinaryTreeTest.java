package com.thealgorithms.datastructures.trees;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SerializeDeserializeBinaryTreeTest {

    @Test
    public void testSerializeNullTree() {
        assertEquals("", SerializeDeserializeBinaryTree.serialize(null));
    }

    @Test
    public void testDeserializeBlankInput() {
        assertNull(SerializeDeserializeBinaryTree.deserialize(" "));
    }

    @Test
    public void testRoundTripCompleteTree() {
        BinaryTree.Node original = TreeTestUtils.createTree(new Integer[] {1, 2, 3, 4, 5, 6, 7});

        String serialized = SerializeDeserializeBinaryTree.serialize(original);
        BinaryTree.Node reconstructed = SerializeDeserializeBinaryTree.deserialize(serialized);

        assertTrue(isSameTree(original, reconstructed));
    }

    @Test
    public void testRoundTripSparseTree() {
        BinaryTree.Node original = TreeTestUtils.createTree(new Integer[] {1, 2, 3, null, 4, null, 5});

        String serialized = SerializeDeserializeBinaryTree.serialize(original);
        BinaryTree.Node reconstructed = SerializeDeserializeBinaryTree.deserialize(serialized);

        assertEquals("1,2,3,null,4,null,5", serialized);
        assertTrue(isSameTree(original, reconstructed));
    }

    @Test
    public void testDeserializeHandlesWhitespace() {
        BinaryTree.Node root = SerializeDeserializeBinaryTree.deserialize("1, 2, 3, null, 4");
        assertEquals("1,2,3,null,4", SerializeDeserializeBinaryTree.serialize(root));
    }

    @Test
    public void testDeserializeRejectsInvalidNumber() {
        assertThrows(IllegalArgumentException.class, () -> SerializeDeserializeBinaryTree.deserialize("1,two,3"));
    }

    @Test
    public void testDeserializeRejectsEmptyToken() {
        assertThrows(IllegalArgumentException.class, () -> SerializeDeserializeBinaryTree.deserialize("1,,3"));
    }

    @Test
    public void testDeserializeRejectsExtraDetachedTokens() {
        assertThrows(IllegalArgumentException.class, () -> SerializeDeserializeBinaryTree.deserialize("1,null,null,2"));
    }

    private static boolean isSameTree(BinaryTree.Node first, BinaryTree.Node second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        return first.data == second.data && isSameTree(first.left, second.left) && isSameTree(first.right, second.right);
    }
}
