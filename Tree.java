import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.*;

public class Tree {

	private String node;
	private List<String> leaves;
	private double prob;

	public Tree(String node, String left, String right, double prob) {
		this.node = node;
		this.leaves = new ArrayList<String>();
		leaves.add(left);
		leaves.add(right);
		this.prob = prob;
	}

	public Tree(String node, String terminal, double prob) {
		this.node = node;
		this.leaves = new ArrayList<String>();
		leaves.add(terminal);
		this.prob = prob;
	}

	public boolean isTerminal() {
		if (leaves.size() == 1) {
			return true;
		}
		return false;
	}

	public String get_node() {
		return node;
	}

	public String get_left_node(String node) {
		return leaves.get(0);
	}

	public String get_right_node(String node) {
		return leaves.get(1);
	}

	public String get_terminal(String node) {
		return leaves.get(0);
	}

	public double get_arc_prob(String node) {
		return prob;
	}

	public void print() {
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("node:" + node);
		System.out.print("leaves:");
		for (int i = 0; i < leaves.size(); i ++) {
			System.out.print(leaves.get(i) + "\t");
		}
		System.out.println();
		System.out.println("probability:" + prob);
	}
}