package cs240.byu.edu.spellcorrector_startingcode_android.StudentPackage;

public class Trie implements ITrie {
    private int word_count;
    private int node_count;
    private Node root;

    public Trie() {
        root = new Node();
        word_count = 0;
        node_count = 1;
    }

    @Override
    public void add(String word) {
        if (word == "") return;
        word = word.toLowerCase();
        root.add(word);
    }

    @Override
    public INode find(String word) {
        if (word == "") return null;
        word = word.toLowerCase();
        return root.find(word);
    }

    public int count(String word) {
        if (word == "") return 0;
        word = word.toLowerCase();
        return root.count(word);
    }

    public Node getRoot() {
        return root;
    }

    @Override
    public int getWordCount() {
        return word_count;
    }

    @Override
    public int getNodeCount() {
        return node_count;
    }

    @Override
    public String toString() {
        return root.toString();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        return (word_count+prime)*prime+(node_count+prime)*prime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Trie)) return false;
        if (((Trie)o).getWordCount() != word_count ||
                ((Trie)o).getNodeCount() != node_count) return false;
        if (!root.equals(((Trie)o).getRoot())) return false;
        return true;
    }

    public class Node implements ITrie.INode {
        private Node[] nodes;
        private int value;

        public Node() {
            value = 0;
            nodes = null;
        }

        public int getValue() {
            return value;
        }

        public Node[] getNodes() {
            return nodes;
        }

        /*MEMORIZE*/
        public void add(String word) {
            if (nodes == null) nodes = new Node[26];
            if (word.length() == 0) {
                if (value == 0) ++word_count;
                ++value;
            } else {
                if (nodes[word.charAt(0)-'a'] == null) {
                    nodes[word.charAt(0)-'a'] = new Node();
                    ++node_count;
                }
                nodes[word.charAt(0)-'a'].add(word.substring(1));
            }
        }

        /*MEMORIZE*/
        public Node find(String word) {
            if (word.length() == 0) {
                if (value != 0) return this;
                return null;
            }
            if (nodes == null || nodes[word.charAt(0)-'a'] == null) return null;
            return nodes[word.charAt(0)-'a'].find(word.substring(1));
        }

        public int count(String word) {
            if (word.length() == 0) return value;
            if (nodes == null || nodes[word.charAt(0)-'a'] == null) return 0;
            return nodes[word.charAt(0)-'a'].count(word.substring(1));
        }

        /*MEMORIZE*/
        public boolean equals(Node node) {
            if ((node.getNodes() == null && nodes != null) ||
                    (node.getNodes() != null && nodes == null)) return false;
            if (node.getValue() != value) return false;
            if (nodes != null) {
                for (int i = 0; i < 26; ++i) {
                    if ((node.getNodes()[i] == null && nodes[i] != null) ||
                            (node.getNodes()[i] != null && nodes[i] == null)) return false;
                    if (nodes[i] != null) {
                        if (!nodes[i].equals(node.getNodes()[i])) return false;
                    }
                }
            }
            return true;
        }

        /*MEMORIZE*/
        public String toString() {
            if (nodes == null) return "";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 26; ++i) {
                if (nodes[i] != null) nodes[i].updateString(sb, Character.toString( (char) ('a'+i) ));
            }
            return sb.toString();
        }

        /*MEMORIZE*/
        public void updateString(StringBuilder sb, String s) {
            if (value > 0) {
                sb.append(s);
                sb.append("\n");
            }
            if (nodes == null) return;
            for (int i = 0; i < 26; ++i) {
                if (nodes[i] != null) nodes[i].updateString(sb, s+Character.toString( (char) ('a'+i) ));
            }
        }

    }
}

