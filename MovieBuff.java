
import java.util.Scanner;

/**
 * Decision Tree Game (binary tree).
 * Internal nodes = yes/no questions, leaves = movie guesses.
 * Left child = yes, right child = no.
 */
public class MovieBuff {

    static class Node {
        String text;
        Node yes, no;

        Node(String text) {
            this.text = text;
        }

        boolean isLeaf() {
            return yes == null && no == null;
        }
    }

    private Node root;
    private final Scanner sc = new Scanner(System.in);

    public MovieBuff() {

        root = new Node("Are you in the mood for a comedy?");

        // Comedy branch
        root.yes = new Node("Do you want a Hindi movie?");
        root.yes.yes = new Node("Do you want a comedy with some emotion in it too?");
        root.yes.yes.yes = new Node("3 Idiots");
        root.yes.yes.no = new Node("Hera Pheri");

        root.yes.no = new Node("Is it animated?");
        root.yes.no.yes = new Node("Shrek");
        root.yes.no.no = new Node("Home Alone");

        // Non-comedy branch
        root.no = new Node("Do you want something thrilling or action-packed?");
        root.no.yes = new Node("Does it have superheroes?");
        root.no.yes.yes = new Node("Avengers: Endgame");
        root.no.yes.no = new Node("Mission: Impossible");

        root.no.no = new Node("Do you want something mind-bending?");
        root.no.no.yes = new Node("Inception");
        root.no.no.no = new Node("Zindagi Na Milegi Dobara");
    }

    // Ask a yes/no question
    private boolean askYesNo(String question) {
        while (true) {
            System.out.print(question + " (y/n): ");
            String s = sc.nextLine().trim().toLowerCase();

            if (s.equals("y") || s.equals("yes"))
                return true;

            if (s.equals("n") || s.equals("no"))
                return false;

            System.out.println("Please answer y or n.");
        }
    }

    // Get non-empty text input
    private String askText(String prompt) {
        String s;

        do {
            System.out.print(prompt);
            s = sc.nextLine().trim();
        } while (s.isEmpty());

        return s;
    }

    // Play one round
    public void play() {
        Node parent = null;
        boolean wentYes = false;
        Node cur = root;

        // Traverse the decision tree
        while (!cur.isLeaf()) {
            boolean ans = askYesNo(cur.text);

            parent = cur;
            wentYes = ans;

            cur = ans ? cur.yes : cur.no;
        }

        // Make a movie recommendation
        if (askYesNo("How about watching " + cur.text + "?")) {
            System.out.println("Perfect! Grab the popcorn and enjoy the movie.\n");
            return;
        }

        // Learn a new movie
        String thing = askText("What would you rather watch? ");

        String question = askText(
            "Give me a yes/no question that is true for "
            + thing + " but false for " + cur.text + ": "
        );

        boolean yesForNew = askYesNo(
            "For " + thing + ", is the answer yes?"
        );

        Node q = new Node(question);
        Node newLeaf = new Node(thing);

        if (yesForNew) {
            q.yes = newLeaf;
            q.no = cur;
        } else {
            q.yes = cur;
            q.no = newLeaf;
        }

        // Replace old leaf with new question
        if (parent == null)
            root = q;
        else if (wentYes)
            parent.yes = q;
        else
            parent.no = q;

        System.out.println("Noted! Movie list updated.\n");
    }

    // Print the decision tree
    public void printTree() {
        System.out.println("\n--- Current decision tree ---");
        print(root, "", "");
        System.out.println();
    }

    private void print(Node n, String prefix, String label) {
        if (n == null)
            return;

        System.out.println(
            prefix + label + (n.isLeaf() ? "-> " : "? ") + n.text
        );

        print(n.yes, prefix + "    ", "[Y] ");
        print(n.no, prefix + "    ", "[N] ");
    }

    // Calculate tree height
    public int height() {
        return height(root);
    }

    private int height(Node n) {
        if (n == null)
            return 0;

        return 1 + Math.max(height(n.yes), height(n.no));
    }

    // Count movie leaves
    public int countLeaves() {
        return countLeaves(root);
    }

    private int countLeaves(Node n) {
        if (n == null)
            return 0;

        if (n.isLeaf())
            return 1;

        return countLeaves(n.yes) + countLeaves(n.no);
    }

    // Main method
    public static void main(String[] args) {

        MovieBuff game = new MovieBuff();

        System.out.println("=== Movie Recommendation Game ===");
        System.out.println(
            "Answer a few questions and I'll pick your movie for tonight.\n"
        );

        do {
            game.play();
        } while (game.askYesNo("Play again?"));

        game.printTree();

        System.out.println(
            "Tree height: " + game.height()
            + " | Movies known: " + game.countLeaves()
        );
    }
}
