/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static  int L;       // number of codewords = 2^W
    private static  int W;         // codeword width
      public static String lzwmodel;      // represent Do nothing, Reset and Monitor mode
      public static int modedecode;  //n==0;r==1;m==2


    public static void compress() { 
        W=9;
        L=512;
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        int i;//the current codewords number
        for (i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF
        i++;
     
       //Nothing Mode
       if(lzwmodel.equals("n")){
         
          BinaryStdOut.write(0,2);
          while(input.length()>0){
           System.err.println("W: " + W+  ", Number of words: " + i + ",  L: " + L );

            if((i>=L)&&(W <16)){
                W++;
                L=(int)Math.pow(2,W);
            }
            
              
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L) {   // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
                i++;
            }
            input = input.substring(t);            // Scan past s in input.
       

          }
      System.err.println("Do Nothing Compression Completed.");
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();

       }
       //Reset Mode
       else if(lzwmodel.equals("r")){
            BinaryStdOut.write(1,2);

          while(input.length()>0){
           System.err.println("W: " + W+  ", Number of words: " + i + ",  L: " + L );

            if((i>=L)&&(W <16)){
                W++;
                L=(int)Math.pow(2,W);
            }
            if(i==65536){//reach MAX number of codewords
                st=new TST<Integer>(); //rest trie
                for(i=0;i<R;i++)
                    st.put(""+(char)i,i);
               code = R+1; 
               i++;

                W=9;
                L=(int)Math.pow(2,W);


            }
              
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L) {   // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
                i++;
            }
            input = input.substring(t);            // Scan past s in input.
       

          }
        System.err.println("Reset Compression Completed.");
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();



       }
       //Monitor Mode
       else if(lzwmodel.equals("m")){
             boolean monitor=false;
             BinaryStdOut.write(2,2);
             double  cR=0.0;//compression radio
             double oldR=0.0;
             int readin=0;//input 
             int compressed=0;//input already compressed
             while(input.length()>0){
           //       System.err.println("W: " + W+  ", Number of words: " + i + ",  L: " + L );
                System.err.println("W : " + W + ", input length: " + input.length() + ", Number OF codewords: " + i + ", L: " + L );

            if((i>=L)&&(W <16)){
                W++;
                L=(int)Math.pow(2,W);
            }
             
            if(compressed !=0) {
                    cR=((double)readin/(double)compressed);}
            if(i==65536||monitor==true){//reach MAX number of codewords(last 16 bits), begin monitor

                if(monitor==false){//first time reach  Max codewords
                  System.err.println("Max number of codewords reach. Starting monitoring.....");
                 
                  oldR=cR;
                  monitor=true;

                }else if((oldR/cR)>1.1){

                  //Reset Trie
                    System.err.println("Radio of compression radio over 1.1, reset TST.");
                st=new TST<Integer>(); 
                for(i=0;i<R;i++)
                    st.put(""+(char)i,i);
               code = R+1; 
               i++;

                W=9;
                L=(int)Math.pow(2,W);
                monitor=false;
                readin=0;
                compressed=0;
                
                }


            }
              
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            readin+=(8*s.length());//how many bits read from input
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            compressed+=W;
            int t = s.length();
            if (t < input.length() && code < L) {   // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
                i++;
            }
            input = input.substring(t);            // Scan past s in input.
       

          }
        System.err.println("Monitor Compression Completed.");
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();

       }else{
       System.err.println("Please the mode 'n', 'r', or 'm' to compress");
       }
      
       /**
        ** Orignal code
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L)    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
      **/   
    } 


    public static void expand() {
         
         W=9;
         L=512;
        modedecode=BinaryStdIn.readInt(2);

        String[] st = new String[65536];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];
        //decode when is nothing mode
        if(modedecode==0){
           System.err.println("Decode: Do Nothing Mode"); 
           while (true) {

              if((i>=L-1)&&(W<16)){
                W++;
                L=(int)Math.pow(2,W);
              }
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }

        }
        else if(modedecode==1){//       decode when is reset mode
            System.err.println("Decode: Reset Mode"); 
                while (true) {

                    if(i==65536-1){
                       st=new String[65536];
                    for(i=0;i<R;i++)
                         st[i]=""+(char)i;
                    st[i++] = "";  
        

                      W=9;
                      L=(int)Math.pow(2,W);
                      codeword = BinaryStdIn.readInt(W);
                      val = st[codeword];
                    }

              if((i>=L-1)&&(W<16)){
                W++;
                L=(int)Math.pow(2,W);
              }
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
  
        }else{       //decode when is monitor mode
            System.err.println("Decode: Monitor Mode"); 
            boolean monitor=false;
             BinaryStdOut.write(2,2);
             double cR=0.0;//compression radio
             double oldR=0.0;
             int readin=0;//input 
             int compressed=0;//input already compressed

          while (true) {


             if((i>=L-1)&&(W<16)){
                W++;
                L=(int)Math.pow(2,W);
              }

              if(compressed!=0) cR=((double)readin/(double)compressed);
             
          /**
         
           
           **/
            codeword = BinaryStdIn.readInt(W);
            compressed+=W;
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
            readin+=(8*val.length());
        }
        }

        
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
      if(args.length==2){
        if(args[1].equals("n")||args[1].equals("r")||args[1].equals("m"))  lzwmodel=args[1];  
       }
          

        if      (args[0].equals("-"))  compress();
           

        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
