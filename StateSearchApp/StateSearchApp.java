import java.util.*;


//? StateSearchApp
//* A tiny application that does the following:
//*  1) Lists all of the names of the 50 U.S. states as the texts input.
//*  2) Displays a simple menu that displays text, Search, and Exit.
//*  3) Searches throughout the text using the Boyer-moore Implementation.

//* Notes:
//*  - Search is case-insensitive by default(We can toggle case sensitive by default).
//*  - We treat the full text as a singular string with newline separators so
//*    lock in positions that are 100% consistent when running the program.
 
public class StateSearchApp {

    //! Set to true if you want case-sensitive search. False if non-case sensitive.
    private static final boolean CASE_SENSITIVE = false;

    //! --- Starting point! ---
    public static void main(String[] args) {
        final String text = buildStatesText();  // input text (names of the 50 states)
        final Scanner in = new Scanner(System.in);

        while (true) {
            printMenu();
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n--- TEXT CONTENT (50 U.S. States) ---");
                    System.out.println(text);
                    System.out.println("-------------------------------------\n");
                    break;

                case "2":
                    System.out.print("\nEnter a pattern to search for (e.g., 'New', 'Carolina'): ");
                    String pattern = in.nextLine();

                    // Prepare text & pattern for case-insensitive search if requested.
                    String searchText   = CASE_SENSITIVE ? text : text.toLowerCase(Locale.ROOT);
                    String searchPattern= CASE_SENSITIVE ? pattern : pattern.toLowerCase(Locale.ROOT);

                    //! Run Boyer–Moore search.
                    List<Integer> indices = boyerMooreBadCharSearch(searchText, searchPattern);

                    //! Report.
                    System.out.println("\nResults:");
                    if (indices.isEmpty()) {
                        System.out.println("  No occurrences found.");
                    } else {
                        System.out.println("  Occurrence count : " + indices.size());
                        System.out.println("  Start indices    : " + indices);
                        // Show small excerpts around each hit to make results tangible.
                        for (int idx : indices) {
                            int start = Math.max(0, idx - 15);
                            int end   = Math.min(text.length(), idx + pattern.length() + 15);
                            String snippet = text.substring(start, end).replaceAll("\\R", " ");
                            System.out.printf("    @%d ...%s...\n", idx, snippet);
                        }
                    }
                    System.out.println();
                    break;

                case "3":
                    System.out.println("\nExiting program. Goodbye!");
                    in.close();
                    return;

                default:
                    System.out.println("Please choose 1, 2, or 3.\n");
                    break;
            }
        }
    }

    //*Prints the main menu. 
    private static void printMenu() {
        System.out.println("Choose an option:");
        System.out.println("  1) Display the text");
        System.out.println("  2) Search");
        System.out.println("  3) Exit program");
        System.out.print("Enter choice: ");
    }

  
    //* Returns the text which is the names of the 50 U.S. states as a single string.
    //* Newlines are used as separators so indices are consistent and the text is legible.
    
    private static String buildStatesText() {
        String[] states = {
            "Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware",
            "Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky",
            "Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi",
            "Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico",
            "New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania",
            "Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont",
            "Virginia","Washington","West Virginia","Wisconsin","Wyoming"
        };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < states.length; i++) {
            sb.append(states[i]);
            if (i < states.length - 1) sb.append("\n");
        }
        return sb.toString();
    }

    //! Boyer–Moore (bad-character rule) implementation

    
    //* Boyer–Moore (bad-char rule). Returns starting indexes of matches.

     
    public static List<Integer> boyerMooreBadCharSearch(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        if (pattern == null || pattern.isEmpty()) return occurrences;

        int n = text.length();
        int m = pattern.length();
        if (m > n) return occurrences;

        //! Build bad character table for all possible bytes (0..255).
        int[] badChar = buildBadCharTable(pattern);

        //! Search
        int s = 0; // s = shift of the pattern with respect to the text
        while (s <= (n - m)) {
            int j = m - 1;

            // Decrease j while characters match
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }

            if (j < 0) {
                // Match found at shift s
                occurrences.add(s);
                // Shift the pattern so that the next character in text aligns
                // with the last occurrence in the pattern; if none, shift by 1
                s += (s + m < n) ? (m - badChar[text.charAt(s + m) & 0xFF]) : 1;
            } else {
                // Bad-character rule shift
                int bcIndex = text.charAt(s + j) & 0xFF;
                int shift = Math.max(1, j - badChar[bcIndex]);
                s += shift;
            }
        }

        return occurrences;
    }

    
    //* Builds the bad-character table:
    //* For every possible character c (0..255), badChar[c] is the last index
    //* at which c appears in the pattern, or -1 if it does not appear.
    
    private static int[] buildBadCharTable(String pattern) {
        int[] table = new int[256]; // ASCII
        Arrays.fill(table, -1);
        for (int i = 0; i < pattern.length(); i++) {
            table[pattern.charAt(i) & 0xFF] = i;
        }
        return table;
    }
}
