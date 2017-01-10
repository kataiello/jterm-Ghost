/* Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class TrieNode {
    // A map from the next character in the alphabet to the trie node containing those words
    private HashMap<Character, TrieNode> children;
    // If true, this node represents a complete word.
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    /**
     * Add the string as a child of this trie node.
     *
     * @param s String representing partial suffix of a word.
     */
    public void add(String s)
    {
        //base case
        //if the string is empty
        if(s.isEmpty() || s == null)
        {
            isWord = true;
        }
        //recursive case
        else
        {
            //if this is not a child yet
            if (!children.containsKey(s.charAt(0))) {
                //put it in the hashmap
                children.put(s.charAt(0), new TrieNode());
            }
            //call recursion
            children.get(s.charAt(0)).add(s.substring(1));
        }
    }

    /**
     * Determine whether this node is part of a complete word for the string.
     *
     * @param s String representing partial suffix of a word.
     * @return
     */
    public boolean isWord(String s)
    {
        //if this is the end of the string
        if(s.isEmpty())
        {
            //is it a word?
            return isWord;
        }
        else
        {
            //if there are any more ways this could go
            if(children.containsKey(s.charAt(0)))
            {
                //call recursion
                return children.get(s.charAt(0)).isWord(s.substring(1));

            }
            //otherwise, not a word
            else
            {
                return false;
            }
        }
    }

    /**
     * Find any complete word with this partial segment.
     *
     * @param s String representing partial suffix of a word.
     * @return
     */
    public String getAnyWordStartingWith(String s) {
        // TODO(you):
        Character head;
        String rest;

        if(s.isEmpty())
        {
            if(isWord)
            {
                return "";
            }
            else
            {
                Set<Character> keyset = children.keySet();
                if(keyset.isEmpty())
                {
                    return null;
                }
                head = keyset.iterator().next();
                rest = "";

            }
        }
        else
        {
            head = s.charAt(0);
            rest = s.substring(1);
            if(!children.containsKey(head))
            {
                return null;
            }
            String word = children.get(head).getAnyWordStartingWith(rest);
            if(word == null)
            {
                return null;
            }
            return head + word;
        }
        return null;
    }


    /**
     * Find a good complete word with this partial segment.
     *
     * Definition of "good" left to implementor.
     *
     * @param s String representing partial suffix of a word.
     * @return
     */
    public String getGoodWordStartingWith(String s)
    {
        //TODO: write a better algorithm
        for(int i = 0; i < 10; i++)
        {
            String word = getAnyWordStartingWith(s);
            if(word.length() % 2 == 1)
            {
                return word;
            }
        }
        return getAnyWordStartingWith(s);
    }
}