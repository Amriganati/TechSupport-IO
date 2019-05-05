import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

/**
 * The responder class represents a response generator object.
 * It is used to generate an automatic response, based on specified input.
 * Input is presented to the responder as a set of words, and based on those
 * words the responder will generate a String that represents the response.
 *
 * Internally, the reponder uses a HashMap to associate words with response
 * strings and a list of default responses. If any of the input words is found
 * in the HashMap, the corresponding response is returned. If none of the input
 * words is recognized, one of the default responses is randomly chosen.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version 2016.02.29
 * 
 * @author Andrew Riganati
 * @version 2019.05.05
 * And thanks goes to Clare (again.) for helping me (again) with both code and reconstructing the constructor and declaration statments after blueJ ate/deleted them.
 */


public class Responder
{
    
       // Used to map key words to responses.
    private HashMap<String, String> responseMap;
    // Default responses to use if we don't recognise a word.
    private ArrayList<String> defaultResponses;
    // The name of the file containing the default responses.
    private static final String FILE_OF_DEFAULT_RESPONSES = "default.txt";
    // The name of the file containing the keywords and their responses.
    private static final String FILE_OF_RESPONSES = "response.txt";
    private Random randomGenerator;
    
    /**
     * Construct a Responder
     */
    public Responder()
    {
        responseMap = new HashMap<>();
        defaultResponses = new ArrayList<>();
        fillResponseMap();
        fillDefaultResponses();
        randomGenerator = new Random();
    }
    
    /**
     * Generate a response from a given set of input words.
     * 
     * @param words A set of words entered by the user
     * @return a string that should be displayed as a repsonse
     * (above wording is clares not mine. Mine was eaten by blue jay when it tried to format this upon over writing or attempting to overwrite bluej fork)
     */
    private void fillResponseMap() {
       Charset charset = Charset.forName("US-ASCII");
       Path path = Paths.get(FILE_OF_RESPONSES);
       try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
           boolean blankline = true; //tracks length of previous line and wheather it was blank
           ArrayList<String> keywords = new ArrayList<String>(); // Stores keywords
           String response = ""; // string to build reponse
           String read = reader.readLine(); // stores individual lines
       
           while(read != null) {
               if(read.trim().length() == 0){
                   //is the line blank/whitespace?
                   blankline = true;
               
                   // Map for keywords and their responses if they exist
                   for (String keyword : keywords){
                   responseMap.put(keyword.trim(), response);
                  }
                  keywords.clear();
                  response = "";
               }
               else {
                   if(blankline) {
                   //previous line was blank, keywords contained in this line
                   keywords = new ArrayList(Arrays.asList(read.split(",")));
                   blankline = false;
                    }
                    else {
                    // Previous line !blank. line contains reponse.
                    response += read + "\n";
                  }
           }
            //get next line
            read = reader.readLine();
         }
      }
        catch(FileNotFoundException e) {
            System.err.println("Unable to open " + FILE_OF_RESPONSES);
            }
        catch(IOException e) {
                System.err.println("A problems was encountered reading " + FILE_OF_RESPONSES);
            }
    }
    
    /**
     * Build up a list of default responses from which we can pick
     * if we don't know what else to say.
     */
    private void fillDefaultResponses()
    {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(FILE_OF_DEFAULT_RESPONSES);
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            boolean blankline = true;
            String response = "";
            String read = reader.readLine();
            while(read != null) {
                 if(read.trim().length() == 0) {
                    // Line is effectively blank.
                    blankline = true;
                    if(response != "") {
                        // response is not empty.
                        defaultResponses.add(response);
                    }
                        } 
                     else {
                        if(blankline) {
                        // Previous line was blank so this line is the start of response.
                        response = read;
                        blankline = false;
                       } 
                        else {
                            // Previous line was not blank so this is a continuation of response.
                            response += read + "\n";
                    }
                    
                }
                read = reader.readLine();
            }
        }
        catch(FileNotFoundException e) {
            System.err.println("Unable to open " + FILE_OF_DEFAULT_RESPONSES);
        }
        catch(IOException e) {
            System.err.println("A problem was encountered reading " +
                               FILE_OF_DEFAULT_RESPONSES);
        }
        // Make sure we have at least one response.
        if(defaultResponses.size() == 0) {
            defaultResponses.add("Could you elaborate on that?");
        }
    }

    /**
     * Randomly select and return one of the default responses.
     * @return     A random default response
     */
    private String pickDefaultResponse()
    {
        // Pick a random number for the index in the default response list.
        // The number will be between 0 (inclusive) and the size of the list (exclusive).
        int index = randomGenerator.nextInt(defaultResponses.size());
        return defaultResponses.get(index);
    }
}
