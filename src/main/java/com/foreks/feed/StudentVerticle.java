package com.foreks.feed;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foreks.feed.model.Student;

public class StudentVerticle extends AbstractVerticle {
    Map<Integer, MyTreeSet<Student>> studentMap       = new HashMap<Integer, MyTreeSet<Student>>();
    Map<String, Runnable>            traverseStrategy = new HashMap<String, Runnable>();
    Map<String, Comparator<Student>> comparatorMap    = new HashMap<String, Comparator<Student>>();
    Comparator<Student>              comparator;
    int                              a;

    @Override
    public void start() throws Exception {
        initMyComparatorMap();
        this.vertx.eventBus().<String> consumer("Create.Student").handler(this::createStudent);
        this.vertx.eventBus().<String> consumer("Add.Student").handler(this::addStudent);
        this.vertx.eventBus().<String> consumer("Delete.Student").handler(this::deleteStudent);
        this.vertx.eventBus().<String> consumer("Update.Student").handler(this::updateStudent);
        this.vertx.eventBus().<String> consumer("Print.String").handler(this::print);
        this.vertx.eventBus().<String> consumer("Size.String").handler(this::size);
    }

    private void initMyComparatorMap() {
        this.comparatorMap.put("comparatorById", Comparator.comparing(Student::getId));
        this.comparatorMap.put("comparatorByName", Comparator.comparing(Student::getName));
        this.comparatorMap.put("comparatorByLastName", Comparator.comparing(Student::getLastName));

        this.comparator = this.comparatorMap.get("comparatorById");
    }

    public void size(final Message<String> m) {
        final MyTreeSet<Student> tree = this.studentMap.get(Integer.parseInt(m.body()));
        m.reply(m.body() + " anahtarli agacin boyutu: " + tree.size());
    }

    public void print(final Message<String> m) {
        final MyTreeSet<Student> tree = this.studentMap.get(Integer.parseInt(m.body()));
        m.reply(m.body() + " anahtarli agacin degerleri: " + tree.toString());
    }

    public void updateStudent(final Message<String> m) {

        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            MyTreeSet<Student> tree = this.studentMap.get(studentTree.getTreeId());
            if (tree == null) {
                final Random random = new Random();
                final int a = random.nextInt();
                tree = new MyTreeSetImpl<Student>("inorder", this.comparatorMap.get("comparatorById"));
                this.studentMap.put(a, tree);
            }
            tree.updateValue(studentTree.getStudent(), studentTree.getNewStudent());

            m.reply(studentTree.getTreeId() + " anahtarli agacin degerleri: " + tree.toString());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void deleteStudent(final Message<String> m) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            MyTreeSet<Student> tree = this.studentMap.get(studentTree.getTreeId());
            if (tree == null) {
                final Random random = new Random();
                final int a = random.nextInt();
                tree = new MyTreeSetImpl<Student>("inorder", this.comparatorMap.get("comparatorById"));
                this.studentMap.put(a, tree);
            }
            tree.remove(studentTree.getStudent());
            m.reply(studentTree.getTreeId() + " anahtarli agacin degerleri: " + tree.toString());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addStudent(final Message<String> m) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            MyTreeSet<Student> tree = this.studentMap.get(studentTree.getTreeId());
            if (tree == null) {
                tree = new MyTreeSetImpl<Student>("inorder", this.comparatorMap.get("comparatorById"));
                this.studentMap.put(studentTree.getTreeId(), tree);
            }
            tree.add(studentTree.getStudent());
            m.reply(studentTree.getTreeId() + " anahtarli agacin degerleri: " + tree.toString());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void createStudent(final Message<String> m) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            this.comparator = this.comparatorMap.get(studentTree.getComparator());
            final MyTreeSet<Student> tree = new MyTreeSetImpl<Student>(studentTree.getStrategy(), this.comparator);
            this.studentMap.put(this.a, tree);
            m.reply(this.a + " anahtarli " + studentTree.getStrategy() + " Student agac olusturuldu.");
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
