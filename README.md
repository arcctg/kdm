## Boolean Expression Simplifier
Enter mode (0 for PCNF, 1 for PDNF) with numbers separated by a space or 'exit' to terminate the program.
### Input Examples
If you want to find PCNF on sets 1, 3, 4, 5, 7, 8, 11, 13, 14, 15, enter:
```
0 1 3 4 5 7 8 11 13 14 15
```
If you want to find the PDNF on sets 0, 2, 6, 9, 10, 12, enter:
```
1 0 2 6 9 10 12
```
If you want to terminate the program, enter:
```
exit
```
### Output example
```
0 0 1 4 5 6 9 10 11 13 14
```
```
PCNF on a set of 0, 1, 4, 5, 6, 9, 10, 11, 13, 14:
(1v2v3v4)(1v2v3v-4)(1v-2v3v4)(1v-2v3v-4)(1v-2v-3v4)(-1v2v3v-4)(-1v2v-3v4)(-1v2v-3v-4)(-1v-2v3v-4)(-1v-2v-3v4)

Implicants:
1) 1-2: 1v2v3
2) 1-3: 1v3v4
3) 2-4: 1v3v-4
4) 2-6: 2v3v-4
5) 3-4: 1v-2v3
6) 3-5: 1v-2v4
7) 4-9: -2v3v-4
8) 5-10: -2v-3v4
9) 6-8: -1v2v-4
10) 6-9: -1v3v-4
11) 7-8: -1v2v-3
12) 7-10: -1v-3v4

Implicants:
1) 1-5: 1v3
  —(2-3: 1v3)
2) 3-10: 3v-4
  —(4-7: 3v-4)
3) 1v-2v4
4) -2v-3v4
5) -1v2v-4
6) -1v2v-3
7) -1v-3v4

Implicants:
1) 1v3
2) 3v-4
3) 1v-2v4
4) -2v-3v4
5) -1v2v-4
6) -1v2v-3
7) -1v-3v4

    1   2   3   4   5   6   7   8   9   10 
 1| X | X | X | X |   |   |   |   |   |   
 2|   | X |   | X |   | X |   |   | X |   
 3|   |   | X |   | X |   |   |   |   |   
 4|   |   |   |   | X |   |   |   |   | X 
 5|   |   |   |   |   | X |   | X |   |   
 6|   |   |   |   |   |   | X | X |   |   
 7|   |   |   |   |   |   | X |   |   | X 

Variants of min CNF:
1) (3v-4)(1v3)(-2v-3v4)(-1v2v-3)

PDNF on a set of 2, 3, 7, 8, 12, 15:
-1-23-4 v -1-234 v -1234 v 1-2-3-4 v 12-3-4 v 1234

Implicants:
1) 1-2: -1-23
2) 2-3: -134
3) 3-6: 234
4) 4-5: 1-3-4

Implicants:
1) -1-23
2) -134
3) 234
4) 1-3-4

    1   2   3   4   5   6 
 1| X | X |   |   |   |   
 2|   | X | X |   |   |   
 3|   |   | X |   |   | X 
 4|   |   |   | X | X |   

Variants of min DNF:
1) 234 v 1-3-4 v -1-23

Enter your query or 'exit' to terminate the program.
```
