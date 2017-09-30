# pathFindingAI

This is a program I wrote originally for a CS 440 (Intro to AI) assignment, but expanded on later for visualization


## Setup

It is recommended that the file be downloaded as a zip, and opened in Eclipse. If you wish to run everything from the command line, including all the Java files, please see the link here: https://stackoverflow.com/questions/6153057/how-to-run-a-java-project-in-command-line. 


### Step 1. Running the Algorithms

#### Step 1a. AStar

Locate the AStar folder within the main project.

To run the main AStar path searching algorithm, run Interface.java, either through commandline or Eclipse. After running the project, you will be given a choice to initialize 50 graphs, run the algorithm for one file, run the algorithm for all 50 files, or use the sequential and incremental algorithms.

Initialize 50 graphs randomly populates textfiles based on a set of probabilities for block tiles.

Running the algorithm will pass through the files based upon a specific heuristic mode that will be selected using user input. The sequential and incremental algorithms work in the same way, requiring additional user input.


#### Step 1b. Filtering & Viterbi

Locate the Filtering & Viterbi algorithms within the Viterbi folder.

To run both Filtering & Viterbi algorithms on the gridworld, you must already have initialized 50 graphs using the AStar Java Program. You must have previously generated grids before you run the filtering and Viterbi Algorithms.

Run Main.java within the Viterbi folder. You will be asked for an input: 0 calculates filtering while 1 will calculate Viterbi and output the shortest likely path. The algorithm will be run on all the files specified.


### Step 2. Visualizing using Python

Locate the dist folder, and run in the command line:

```
$ python mapgen2.py
```

This will open the GUI to cycle through heatmaps of all the given Viterbi Graphs. This will not work unless the files were already created.

If there are any errors, you are missing a .dll file or try running the .exe.

## Credit

Built by Benjamin Yang at Rutgers University, kept under the MIT License.
