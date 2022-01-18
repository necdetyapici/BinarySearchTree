package com.foreks.feed;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.foreks.feed.MyTreeSet;
import com.foreks.feed.MyTreeSetImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StringVerticle extends AbstractVerticle {
    Map<Integer, MyTreeSet<String>> stringMap           = new HashMap<Integer, MyTreeSet<String>>();
    Map<String, Runnable>           traverseStrategyMap = new HashMap<String, Runnable>();
    Comparator<String>              comparator          = Comparator.comparing(String::toString);
    int                             a;

    @Override
    public void start() throws Exception {
        this.vertx.eventBus().<String> consumer("Create.String").handler(this::createString);
        this.vertx.eventBus().<String> consumer("Add.String").handler(this::addString);
        this.vertx.eventBus().<String> consumer("Delete.String").handler(this::deleteString);
        this.vertx.eventBus().<String> consumer("Update.String").handler(this::updateString);
        this.vertx.eventBus().<String> consumer("Print.String").handler(this::print);
        this.vertx.eventBus().<String> consumer("Size.String").handler(this::size);
    }

    public void size(final Message<String> m) {
        final MyTreeSet<String> tree = this.stringMap.get(Integer.parseInt(m.body()));
        m.reply(m.body() + " anahtarli agacin boyutu " + tree.size());
    }

    public void print(final Message<String> m) {
        final MyTreeSet<String> tree = this.stringMap.get(Integer.parseInt(m.body()));
        m.reply(m.body() + " anahtarli agacin degerleri: " + tree.toString());
    }

    public void updateString(final Message<String> m) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            MyTreeSet<String> tree = this.stringMap.get(studentTree.getTreeId());
            if (tree == null) {
                tree = new MyTreeSetImpl<String>("inorder", this.comparator);
                this.stringMap.put(studentTree.getTreeId(), tree);
            }
            tree.updateValue(studentTree.getValueString(), studentTree.getNewValueString());
            m.reply(studentTree.getTreeId() + " anahtarli agacin degerleri: " + tree.toString());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void deleteString(final Message<String> m) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            MyTreeSet<String> tree = this.stringMap.get(studentTree.getTreeId());
            if (tree == null) {
                tree = new MyTreeSetImpl<String>("inorder", this.comparator);
                this.stringMap.put(studentTree.getTreeId(), tree);
            }
            tree.remove(studentTree.getValueString());
            m.reply(studentTree.getTreeId() + " anahtarli agacin degerleri: " + tree.toString());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addString(final Message<String> m) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            MyTreeSet<String> tree = this.stringMap.get(studentTree.getTreeId());
            if (tree == null) {
                tree = new MyTreeSetImpl<String>("inorder", this.comparator);
                this.stringMap.put(studentTree.getTreeId(), tree);
            }
            tree.add(studentTree.getValueString());
            m.reply(studentTree.getTreeId() + " anahtarli agacin degerleri: " + tree.toString());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void createString(final Message<String> m) {

        final ObjectMapper mapper = new ObjectMapper();
        final Random random = new Random();
        final int a = random.nextInt(1000);
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            final MyTreeSet<String> tree = new MyTreeSetImpl<String>(studentTree.getStrategy(), this.comparator);
            this.stringMap.put(this.a, tree);
            m.reply(a + " anahtarli " + studentTree.getStrategy() + " String agac olusturuldu.");
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
