package com.foreks.feed;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import com.foreks.feed.MyTreeSet;
import com.foreks.feed.MyTreeSetImpl;

public class ToStringTask extends RecursiveTask<String> {
    private static final long         serialVersionUID = -8154159104987465931L;
    private final MyTreeSet<Integer>  tree;
    private final int                 nodeCount;
    private final static ForkJoinPool forkJoinPool     = new ForkJoinPool(4);

    public ToStringTask(final MyTreeSet<Integer> tree, final int nodeCount) {
        this.tree = tree;
        this.nodeCount = nodeCount;
    }

    @Override
    public String compute() {
        if (this.nodeCount > 10000) {
            final List<ToStringTask> subtasks = new ArrayList<ToStringTask>();
            subtasks.addAll(createSubtasks());
            final StringBuilder sB = new StringBuilder();
            for (final ToStringTask subtask : subtasks) {
                subtask.fork();
            }
            sB.append(subtasks.get(0).join()).append(this.tree.getValue()).append(subtasks.get(1));
            return sB.toString();
        } else {
            return this.tree.toString();
        }

    }

    private List<ToStringTask> createSubtasks() {
        final List<ToStringTask> subtasks = new ArrayList<ToStringTask>();

        final ToStringTask subtask1 = new ToStringTask(this.tree.getLeftChild(), this.nodeCount / 2);
        final ToStringTask subtask2 = new ToStringTask(this.tree.getRightChild(), this.nodeCount / 2);

        subtasks.add(subtask1);
        subtasks.add(subtask2);

        return subtasks;
    }

    public static void main(final String[] args) {
        final MyTreeSetImpl<Integer> tree = new MyTreeSetImpl<Integer>("inorder", Comparator.naturalOrder());
        new Random().ints(100000, 1, 1000000).forEach(tree::add);
        System.out.println("!!!results!!!");
        singleCore(tree);
        multiCore(tree);
    }

    private static void multiCore(final MyTreeSetImpl<Integer> tree) {
        final ToStringTask myRecursiveTask = new ToStringTask(tree, 100000);
        final Instant before2 = Instant.now();
        final String toAnotherString = forkJoinPool.invoke(myRecursiveTask);
        final Instant after2 = Instant.now();
        System.out.println(" in " + Duration.between(before2, after2).toMillis() + " miliseconds result: " + toAnotherString.substring(1, 100));
    }

    private static void singleCore(final MyTreeSetImpl<Integer> tree) {
        final Instant before = Instant.now();
        final String toString = tree.toString();
        final Instant after = Instant.now();
        System.out.println(" in " + Duration.between(before, after).toMillis() + " miliseconds result: " + toString.substring(1, 100));
    }
}