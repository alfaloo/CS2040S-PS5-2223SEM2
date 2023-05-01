import java.util.ArrayList;
import java.util.Arrays;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';
    public TrieNode root;

    private class TrieNode {
        // String of length 1 or "leaf"
        public String key;
        // first 10 are numbers, next 26 are capital letters,
        // next 26 are lower case letters, last one indicates whether it is a leaf node.
        public TrieNode[] presentChars = new TrieNode[63];
        public TrieNode(String s) {
            this.key = s;
            for(TrieNode i : presentChars) {
                i = null;
            }
        }
    }

    public Trie() {
        this.root = new TrieNode("root");
    }

    //Returns the child node with key of c, null if such child is not present.
    TrieNode hasChild(TrieNode node, char c) {
        int ascii = (int) c;
        if (48 <= ascii && ascii <= 57) {
            return node.presentChars[ascii-48];
        } else if (65 <= ascii && ascii <= 90) {
            return node.presentChars[ascii-55];
        } else if (97 <= ascii && ascii <= 122) {
            return node.presentChars[ascii-61];
        } else {
            return null;
        }
    }

    // Returns the index of child node with key of c in presentChars
    int charPos(char c) {
        int ascii = (int) c;
        if (48 <= ascii && ascii <= 57) {
            return ascii-48;
        } else if (65 <= ascii && ascii <= 90) {
            return ascii-55;
        } else if (97 <= ascii && ascii <= 122) {
            return ascii-61;
        } else {
            return -1;
        }
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        int pointer = 0;
        TrieNode node = root;
        while (pointer < s.length()) {
            if (hasChild(node, s.charAt(pointer)) == null) {
                node.presentChars[charPos(s.charAt(pointer))] =
                        new TrieNode(String.valueOf(s.charAt(pointer)));
                node = hasChild(node, s.charAt(pointer));
                pointer += 1;
            } else {
                node = hasChild(node, s.charAt(pointer));
                pointer += 1;
            }
        }
        if (node.presentChars[62] == null) {
            node.presentChars[62] = new TrieNode("leaf");
        }
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        int pointer = 0;
        TrieNode node = root;
        while (pointer < s.length()) {
            if (hasChild(node, s.charAt(pointer)) == null) {
                return false;
            } else {
                node = hasChild(node, s.charAt(pointer));
                pointer += 1;
            }
        }
        if (node.presentChars[62] != null && node.presentChars[62].key == "leaf") {
            return true;
        }
        return false;
    }

    ArrayList<String> advancedSearch(String condition, String current, TrieNode node) {
        if (condition.length() == 0) {
            ArrayList<String> result = new ArrayList<String>();
            if (node.presentChars[62] != null) {
                result.add(current);
            }
            for (TrieNode i : node.presentChars) {
                if (i != null) {
                    String temp = current + i.key;
                    if (advancedSearch("", temp, i) != null) {
                        result.addAll(advancedSearch("", temp, i));
                    }
                }
            }
            return result;
        } else if (String.valueOf(condition.charAt(0)).equals(".")) {
            ArrayList<String> result = new ArrayList<>();
            for (TrieNode i : node.presentChars) {
                if (i != null && i.key != "leaf") {
                    String temp = current + i.key;
                    if (advancedSearch(condition.substring(1), temp, i) != null) {
                        result.addAll(advancedSearch(condition.substring(1), temp, i));
                    }
                }
            }
            return result;
        } else {
            char c = condition.charAt(0);
            if (hasChild(node, c) != null) {
                ArrayList<String> result = new ArrayList<>();
                String temp = current + String.valueOf(c);
                if (advancedSearch(condition.substring(1), temp, hasChild(node, c)) != null) {
                    result.addAll(advancedSearch(condition.substring(1), temp, hasChild(node, c)));
                }
                return result;
            } else {
                return null;
            }
        }
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        ArrayList<String> allResult = advancedSearch(s, "", root);
        int range;
        if (limit < allResult.size()) {
            range = limit;
        } else {
            range = allResult.size();
        }
        for(int i = 0; i < range; i++) {
            results.add(allResult.get(i));
        }
    }


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        t.insert("abba");
        t.insert("abbde");
        t.insert("abcd");
        t.insert("abcdef");
        t.insert("abd");
        t.insert("abed");
        t.insert("dbec");

        String[] result1 = t.prefixSearch("", 15);
        String[] result2 = t.prefixSearch(".", 15);
        System.out.println(Arrays.toString(result1));
        System.out.println(Arrays.toString(result2));
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
