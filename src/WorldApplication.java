/*
 * Wumpus-Lite, version 0.21 alpha
 * A lightweight Java-based Wumpus World Simulator
 *
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
 *
 * Thanks to everyone who provided feedback and
 * suggestions for improving this application,
 * especially the students from Professor
 * Gmytrasiewicz's Spring 2007 CS511 class.
 *
 * Last modified 4/14/08
 *
 * DISCLAIMER:
 * Elements of this application were borrowed from
 * the client-server implementation of the Wumpus
 * World Simulator written by Kruti Mehta at
 * The University of Texas at Arlington.
 *
 */

import java.io.*;
import java.util.*;

class WorldApplication {

	private static String VERSION = "v0.21a";
	String gameboard = "";
	String out = "";
	int numTrials;
	int maxSteps;
	int worldSize;

	public static void main(String args[]) throws Exception {

		WorldApplication wa = new WorldApplication();

		boolean nonDeterministicMode = false;

		if (wa.readPara(args) == 5) {
			FileWriter fw = new FileWriter(wa.out);

			int trialScores[] = new int[wa.numTrials];
			String trialStateSeqs[] = new String[wa.numTrials];
			int totalScore = 0;

			for (int currTrial = 0; currTrial < wa.numTrials; currTrial++) {

				char[][][] wumpusWorld = readWumpusWorld(wa.worldSize, wa.gameboard);

				Environment wumpusEnvironment = new Environment(wa.worldSize, wumpusWorld);

				Simulation trial = new Simulation(wumpusEnvironment, wa.maxSteps, nonDeterministicMode); // ,
																											// outputWriter,
																											// nonDeterministicMode);

				trialScores[currTrial] = trial.getScore();
				trialStateSeqs[currTrial] = trial.getStateSeq();

			}

			for (int i = 0; i < wa.numTrials; i++) {

				fw.write("\nTrial " + (i + 1) + " score: " + trialScores[i] + "\n");
				fw.write("Trial " + (i + 1) + " StateSeq: " + trialStateSeqs[i] + "\n");

				totalScore += trialScores[i];

			}

			fw.write("\nNumber of trials: " + wa.numTrials + "\n");
			fw.write("Total Score: " + totalScore + "\n");
			fw.write("Average Score: " + ((double) totalScore / (double) wa.numTrials) + "\n");

			fw.close();

		} else {
			wa.usage();
		}

	}

	private void usage() {

		System.out.println("Usage:\n\n-i gameboard.txt");
		System.out.println("-o output.txt");
		System.out.println("-n number of trails");
		System.out.println("-ms max steps");
		System.out.println("-ws world size");

		System.out.println("\njava WorldApplication -i gameboard.txt -o output.txt -n 1 -ms 50 -ws 4");

	}

	private int readPara(String args[]) {

		int n = 0;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-i")) {
				this.gameboard = args[i + 1];
				n++;
			} else if (args[i].equals("-o")) {
				this.out = args[i + 1];
				n++;
			} else if (args[i].equals("-n")) {
				this.numTrials = Integer.parseInt(args[i + 1]);
				n++;
			} else if (args[i].equals("-ms")) {
				this.maxSteps = Integer.parseInt(args[i + 1]);
				n++;
			} else if (args[i].equals("-ws")) {
				this.worldSize = Integer.parseInt(args[i + 1]);
				n++;
			}
		}

		return n;
	}

	public static char[][][] readWumpusWorld(int size, String gameboard) throws Exception {
		char [][][]pan=new char[size][size][4];
		for(int i=0;i<size;++i)
			for(int j=0;j<size;++j)
				for(int k=0;k<4;++k)
					pan[i][j][k]=' ';
		File inputFile=new File(gameboard);
		BufferedReader br=new BufferedReader(new FileReader(inputFile));
		
		for(int i=0;i<size;++i) {
			br.readLine();	
			for(int j=0;j<2;++j) {
				String str=br.readLine();
				for(int k=0;k<size;++k) {
					pan[i][k][j*2]=str.charAt(k*6+2);
					pan[i][k][j*2+1]=str.charAt(k*6+4);
					
				}
			}
		}
		br.close();
		return pan;

//	initialize wumpus world from wumpusWorld.txt
	}
}
