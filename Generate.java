import java.io.*;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class Generate{
    
    static HashSet<String> read(HashSet<String> wordsHashSet){
        File file = new File("words.txt");
        try{
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        br.readLine();
        while((st = br.readLine()) != null){
            wordsHashSet.add(st);
        }
        br.close();
        }
        catch(Exception e){
            System.out.println(e);
        }

        return wordsHashSet;
    }

    public static void main(String[] args) {
        HashSet<String> wordsHashSet = new HashSet<String>(); 
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

        if(args != null){
            read(wordsHashSet);
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
                            if(wordsHashSet.contains(temp)){
                                genwords.add(temp);
                                System.out.println(temp);
                            }

                        }
                    }else{
                        for(c=wordLength; c>=1; c--){
                            for(i=move-1; i>0; i--)
                                if(c == option[i][nopts[i]])
                                    break;
                            if(!(i>0))
                                option[move][++nopts[move]] = c;
                        }
                    }
                }else{
                    nopts[--move]--;
                }
            }
        }else{
            System.out.println("Missing arguments.");
        }
        long endTime   = System.nanoTime();
        // System.out.println(TimeUnit.NANOSECONDS.toSeconds(endTime-startTime));
    }
}