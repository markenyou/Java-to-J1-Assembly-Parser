/* Christopher Tam
   Homework 1
   CPS 425 
   February 11, 2016

   Problem 6.7: Top-down stack parser for

   1) S -> bSb
   2) S -> cAc
   3) A -> bAA
   4) A -> cASAb
   5) A -> dcb
*/
import java.util.*;  // import Stack and Scanner classes
//======================================================
class parser
{
  public static void main(String[] args)
  {
    // construct token manager
    ArgsTokenMgr tm = new ArgsTokenMgr(args);

    // construct parser, pass it the token manager
    Fig0612Parser parser = new Fig0612Parser(tm); 

    parser.parse();                    // do parse
  }
}                                      
//======================================================
class ArgsTokenMgr
{
  private int index;
  String input;
  //-----------------------------------------
  public ArgsTokenMgr(String[] args) 
  {
    if (args.length > 0)
      input = args[0];
    else  // treat no command line arg as null string
      input = "";
    index = 0;
    System.out.println("input = " + input);
  }                            
  //-----------------------------------------
  public char getNextToken() 
  {
    if (index < input.length())
      return input.charAt(index++); // return next char
    else
      return '#';              // # signals end of input
  }
}                                 // end of ArgsTokenMgr
//======================================================
class Fig0612Parser
{
  private ArgsTokenMgr tm;         // token manager
  private Stack<Character> stk;    // stack for parser
  private char currentToken;       // current token
  //-----------------------------------------
  public Fig0612Parser(ArgsTokenMgr tm)
  {
    this.tm = tm;                  // save tm  
    advance();                     // prime currentToken
    stk = new Stack<Character>();  // create stack
    stk.push('$');                 // mark stack bottom
    stk.push('S');                 // push start symbol
  }
  //-----------------------------------------
  private void advance()
  {
    // get next token and save in currentToken
    currentToken = tm.getNextToken();
  }
  //-----------------------------------------
  public void parse()
  {
    boolean done = false;      // controls loop exit

    while (!done) 
    { 
      switch(stk.peek()) 
      {
        case 'S': 
          if (currentToken == 'b') 
          { 
            stk.pop();
            stk.push('b');
            stk.push('S');
            advance();
          }  
          else if (currentToken == 'c') 
          {
            stk.pop();         
            stk.push('c');
            stk.push('A');
            advance();
          }
          else
            done = true;       // exit on reject config
          break;

        case 'A':
          if (currentToken == 'b') 
          {
            stk.pop();
            stk.push('A');
            stk.push('A');
            advance();
          }
          else if (currentToken == 'c') 
          {
            stk.pop();         
            stk.push('b');
            stk.push('A');
            stk.push('S');
            stk.push('A');
            advance();
          }    
          else if (currentToken == 'd') 
          {
            stk.pop();         
            stk.push('b');
            stk.push('c');
            advance();
          }
          else
            done = true;       // exit on reject config
          break;

        case 'b':
        case 'c':
        case 'd':
          if (stk.peek().charValue() == currentToken) 
          {
            stk.pop();    // discard terminal on stack 
            advance();    // discard matching input
          }
          else
            done = true;  // exit on reject config
          break;

        case '$':         // exit on empty stack
          done = true;   
          break;
      }                   
    }                     // end of while

    // test if in accept configuration
    if (currentToken =='#' && stk.peek() == '$')
      System.out.println("accept");
    else
      System.out.println("reject");
  }
}
