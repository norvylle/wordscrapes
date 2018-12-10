
import java.util.HashMap; 
import java.io.*;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
// Trie Node, which stores a character and the children in a HashMap 
class TrieNode { 
    public TrieNode(char ch)  { 
        value = ch; 
        children = new HashMap<>(); 
    } 
    public HashMap<Character,TrieNode> getChildren() {   return children;  } 
    public char getValue()                           {   return value;     } 
  
    private char value; 
    private HashMap<Character,TrieNode> children; 
} 
  
// Implements the actual Trie 
class Trie { 
    // Constructor 
    public Trie()   {     root = new TrieNode((char)0);       }     
  
    // Method to insert a new word to Trie 
    public void insert(String word)  { 
  
        // Find length of the given word 
        int length = word.length(); 
        TrieNode crawl = root; 
  
        // Traverse through all characters of given word 
        for( int level = 0; level < length; level++) 
        { 
            HashMap<Character,TrieNode> child = crawl.getChildren(); 
            char ch = word.charAt(level); 
  
            // If there is already a child for current character of given word 
            if( child.containsKey(ch)){ 
                crawl = child.get(ch);
            } 
            else   // Else create a child 
            { 
                TrieNode temp = new TrieNode(ch);
                child.put( ch, temp ); 
                crawl = temp; 
            } 
        }  
    } 
  
    public boolean search(String input)  { 
        TrieNode crawl = root;
        int length = input.length();    
        int level; 
        
        for( level = 0 ; level < length; level++ ){ 
            char ch = input.charAt(level);
            HashMap<Character,TrieNode> child = crawl.getChildren();                     
            if( child.containsKey(ch)) crawl = child.get(ch);
            else break;
        }
        
        if(level == length) return true;
        else return false;
    } 
  
    private TrieNode root; 
} 
// source: https://www.geeksforgeeks.org/longest-prefix-matching-a-trie-based-solution-in-java/

  
// Testing class 
public class Generate {

    static Trie read(HashSet<String> words){
        Trie dict = new Trie();
        File file = new File("words.txt");
        try{
        BufferedReader br = new BufferedReader(new FileReader(file));
    
        String st;
        br.readLine();
        while((st = br.readLine()) != null){
            dict.insert(st);
            words.add(st);
        }
        br.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    
        return dict;
    }
   public static void main(String[] args) { 
         
        HashSet<String> words = new HashSet<String>();
        HashSet<String> genwords = new HashSet<String>();
        String letters;
        String toFind;
        String temp;
        int wordLength;
        int toFindLength;
        int[] nopts;
        int[][] option;
        int start, move, i, c, checker;
        long startTime = System.nanoTime();
        
        Trie dict = read(words);
        if(args != null){
           
            letters = args[0];
            toFind = args[1];
            wordLength = args[0].length();
            toFindLength = args[1].length();
            
            nopts = new int[toFindLength + 2];
            option = new int[wordLength + 2][wordLength + 2];

            if(toFindLength > wordLength){
                System.out.println("Search Length longer than Word Length");
                return;
            }

            move = start = 0;
            nopts[start] = 1;

            while(nopts[start] > 0){
                
            
                if(nopts[move] > 0){
                    nopts[++move] = 0;
                    if(move == toFindLength + 1){
                        temp = "";
                        for(i=1; i<=toFindLength; i++){
                            temp = temp + letters.charAt(option[i][nopts[i]]-1);
                        }
                        checker = 1;
                        for (i=0; i < toFindLength; i++) {
                            if(toFind.charAt(i) != '_'){
                                if(toFind.charAt(i) == temp.charAt(i)){
                                    continue;
                                }else{
                                    checker = 0;
                                    break;
                                }
                            }
                        }
                        if(checker == 1 && !genwords.contains(temp)){  
                            if(words.contains(temp)){
                                genwords.add(temp);
                                System.out.println(temp);
                            }
                            
                        }
                    }else{
                        
                        
                        for(c=wordLength; c>=1; c--){
                            
                            for(i=move-1; i>0; i--)
                                if(c == option[i][nopts[i]])
                                    break;
                            if(!(i>0)){
                                option[move][++nopts[move]] = c;
                            }

                            
                        }

                        temp = "";
                        for(i=1; i<=move; i++){
                            temp = temp + letters.charAt(option[i][nopts[i]]-1);
                        }
                        
                        while(!dict.search(temp)){
                            if(move != 0 && nopts[move] != 1 ){
                                nopts[move]--;
                                temp = "";
                                for(i=1; i<=move; i++){
                                    temp = temp + letters.charAt(option[i][nopts[i]]-1);
                                }
                                continue;
                            }
                            break;
                            
                        }
                        
                    }

                }else{
                    nopts[--move]--;
                }
            }
        }else{
            System.out.println("Missing arguments.");
        }
        long endTime = System.nanoTime();
        System.out.println(TimeUnit.NANOSECONDS.toSeconds(endTime - startTime)+" seconds");
        // System.out.println(dict.search(args[0]));
    } 
}
