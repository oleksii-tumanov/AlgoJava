package com.thealgorithms.datastructures.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Utility class to serialize and deserialize a binary tree.
 *
 * <p>The tree is serialized in level-order using comma-separated tokens and {@code null}
 * placeholders. Trailing {@code null} tokens are removed to keep the output canonical.
 *
 * <p>Example:
 *
 * <pre>
 *         1
 *        / \
 *       2   3
 *        \
 *         4
 *
 * Serialized form: "1,2,3,null,4"
 * </pre>
 */
public final class SerializeDeserializeBinaryTree {
    private static final String NULL_TOKEN = "null";
    private static final String SEPARATOR = ",";

    private SerializeDeserializeBinaryTree() {
    }

    /**
     * Serializes a binary tree into a comma-separated level-order string.
     */
    public static String serialize(BinaryTree.Node root) {
        if (root == null) {
            return "";
        }

        List<String> tokens = new ArrayList<>();
        Queue<BinaryTree.Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            BinaryTree.Node node = queue.poll();
            if (node == null) {
                tokens.add(NULL_TOKEN);
                continue;
            }
            tokens.add(String.valueOf(node.data));
            queue.add(node.left);
            queue.add(node.right);
        }

        int last = tokens.size() - 1;
        while (last >= 0 && NULL_TOKEN.equals(tokens.get(last))) {
            last--;
        }

        if (last < 0) {
            return "";
        }
        return String.join(SEPARATOR, tokens.subList(0, last + 1));
    }

    /**
     * Deserializes a comma-separated level-order string into a binary tree.
     */
    public static BinaryTree.Node deserialize(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }

        String[] rawTokens = data.split(SEPARATOR, -1);
        String firstToken = sanitizeToken(rawTokens[0]);
        if (isNullToken(firstToken)) {
            if (rawTokens.length == 1) {
                return null;
            }
            throw new IllegalArgumentException("Invalid input: root cannot be null with extra tokens.");
        }

        BinaryTree.Node root = createNode(firstToken);
        Queue<BinaryTree.Node> queue = new LinkedList<>();
        queue.add(root);

        int index = 1;
        while (!queue.isEmpty() && index < rawTokens.length) {
            BinaryTree.Node current = queue.poll();

            String leftToken = sanitizeToken(rawTokens[index++]);
            if (!isNullToken(leftToken)) {
                BinaryTree.Node leftNode = createNode(leftToken);
                current.left = leftNode;
                queue.add(leftNode);
            }

            if (index < rawTokens.length) {
                String rightToken = sanitizeToken(rawTokens[index++]);
                if (!isNullToken(rightToken)) {
                    BinaryTree.Node rightNode = createNode(rightToken);
                    current.right = rightNode;
                    queue.add(rightNode);
                }
            }
        }

        if (hasUnconsumedNonNullTokens(rawTokens, index)) {
            throw new IllegalArgumentException("Invalid input: extra tokens without corresponding parent nodes.");
        }

        return root;
    }

    private static boolean hasUnconsumedNonNullTokens(String[] tokens, int start) {
        for (int i = start; i < tokens.length; i++) {
            if (!isNullToken(sanitizeToken(tokens[i]))) {
                return true;
            }
        }
        return false;
    }

    private static BinaryTree.Node createNode(String token) {
        try {
            return new BinaryTree.Node(Integer.parseInt(token));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid token in serialized data: \"" + token + "\"", exception);
        }
    }

    private static String sanitizeToken(String token) {
        String sanitized = token == null ? "" : token.trim();
        if (sanitized.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: empty token in serialized data.");
        }
        return sanitized;
    }

    private static boolean isNullToken(String token) {
        return NULL_TOKEN.equalsIgnoreCase(token);
    }
}
