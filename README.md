# EDI-Deserializer
EDI Deserializer is a tool software for Deserializing a string which has several sections divided by vertical bar into a string array whose each element value equals one section in the string.
For example, a string "aaaa|bbbb|cccc" can be converted to a string array ["aaaa","bbbb","cccc"]. 
If a vertical bar "|" is a plain char, not a splitter, a question mark would be added before it, such as "aaaa?|bbbb|cccc". In this case, the result array should be ["aaaa|bbbb","cccc"]。
If a question mark "?" is a plain char, not a splitter, a question mark would be added before it, such as "aaaa??bbbb|cccc". In this case, the result array should be ["aaaa?bbbb","cccc"], another example is "aaaa???|bbbb|cccc" whose converted result array should be ["aaaa?|bbbb","cccc"]。  
