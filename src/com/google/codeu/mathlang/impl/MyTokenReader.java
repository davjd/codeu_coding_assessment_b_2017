// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.*;
import com.google.codeu.mathlang.parsing.TokenReader;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {
  private String source;
  private int start;

  public MyTokenReader(String source) {
    // Your token reader will only be given a string for input. The string will
    // contain the whole source (0 or more lines).

    this.source = source;
    start = 0;
  }

  @Override
  public Token next() throws IOException {
    // Most of your work will take place here. For every call to |next| you should
    // return a token until you reach the end. When there are no more tokens, you
    // should return |null| to signal the end of input.

    // If for any reason you detect an error in the input, you may throw an IOException
    // which will stop all execution.

    String current = peek();
    for(int i = 0; i < current.length(); ++i){
      char token = Character.toLowerCase(current.charAt(i));

      if(token == '"'){
        int end = current.indexOf('"', i + 1);

        if(end != -1){
          start = end + 1;
          return new StringToken(current.substring(i + 1, end));
        }
        else throw new IOException("String has a closing quote missing.");
      }
      else if(isSymbol(token)){
        start++;
        return new SymbolToken(token);
      }
      else if(Character.isDigit(token)){
        String number = "";

        int j = i + 1;
        for(; j < current.length() && Character.isDigit(current.charAt(j)); ++j){
          number += current.charAt(j);
        }

        start = j;
        return new NumberToken(Double.parseDouble(number));
      }
      else if(token != ';'){
        String name = "";

        int j = i + 1;
        for(; j < current.length() && current.charAt(j) != ' '; ++j){
          name += current.charAt(j);
        }

        start = j;
        return new NameToken(name);
      }
      else return null;
    }
    return null;
  }

  private boolean isSymbol(char token) {
    return (token == '+' || token == '-' || token == '=');
  }

  private String clear(String in, char c, int from){
    if(in.indexOf(c, from) == -1) return null;
    else return in.substring(from,in.indexOf(c)).replaceAll("\\s+","");
  }

  private String peek(){
    return source.substring(start);
  }

  private boolean isFunctionValid(String function){
    switch (function.substring(0,1).toLowerCase()){
      case "l":
        // let function
        return (function.length() > 2 && function.substring(0, 3) == "let");

      case "p":
        // print function
        return (function.length() > 4 && function.substring(0, 5) == "print");

      case "n":
        // note function
        return (function.length() > 3 && function.substring(0, 4) == "note");

      default: return false;
    }
  }

}
