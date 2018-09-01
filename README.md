# SFG
   ##### It is a Signal flow graph representation of a system , a method used to check stability of control systems.

- ### Main features  
  - User friendly gui made with javafx .
  - Drawing without any restrictions on the position of node .
  - Adding the node’s name and the arrow gain immediately on the draw .
  - The the connections between the nodes are designed to be either straight line or curve to avoid intersections (most of the    time).
  - Showing the solution with the graph to track forward paths.
  - The user can switch between forward paths ,loops ,untouched loops and the total transfer function with single click .
  - If any error happened the user will be notified that his graph has an error in the solution pane.

- ### Data structures
  - Class for nodes, Arrows, loops and forward paths.
  - Arraylists to keep nodes, Arrows, forward paths and loops.
  - Arraylist of Arraylists to keep untouched loops.
  - Hashmap to store the icons with their names as keys
  - Linked Lists for the gui nodes and arrows .
  
- ### Main modules  
  - Control module called SFG that handle all operations .
  - View module to handle GUI .
  - Structure Module that keep all data (Nodes, Arrows, Forward paths  and loops).

- ### Algorithms used
  - DFS to find the forward paths.
  - DFS to find loopst.
  - To find untouched loops, I found all ….. And removed touched and repeated ones.

- ### user guide
<img src="https://github.com/Magho/SFG/blob/master/images/1.png" width="400"> <img src="https://github.com/Magho/SFG/blob/master/images/2.png" width="400"> <img src="https://github.com/Magho/SFG/blob/master/images/3.png" width="400"> 
 
- ### sample runs
<img src="https://github.com/Magho/SFG/blob/master/images/4.png" width="400"> <img src="https://github.com/Magho/SFG/blob/master/images/5.png" width="400"> <img src="https://github.com/Magho/SFG/blob/master/images/6.png" width="400"> <img src="https://github.com/Magho/SFG/blob/master/images/7.png" width="400">  <img src="https://github.com/Magho/SFG/blob/master/images/8.png" width="400"> 

## Running

#### Clone & install
* 
* Clone this repo `https://github.com/Magho/SFG`
* cd `SFG/jar`
* run `SFG.jar` file

## Authors
  - [Magho](https://github.com/Magho)
  - [Abdelrahman Ahmed](https://github.com/abdelrahman882)

## License 
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)
[LICENSE.md](https://github.com/Magho/SFG/blob/master/LICENSE.md)
