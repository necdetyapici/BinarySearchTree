package com.foreks.feed;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

public class IntVerticle extends AbstractVerticle {
    private final Map<Integer, MyTreeSet<Integer>> intMap     = new HashMap<Integer, MyTreeSet<Integer>>();
    private final Comparator<Integer>              comparator = Comparator.naturalOrder();
    private int                                    a;

    @Override
    public final void start() throws Exception {
        this.vertx.eventBus().<String> consumer("Create.Integer").handler(this::createInt);
        this.vertx.eventBus().<String> consumer("Add.Integer").handler(this::addInt);
        this.vertx.eventBus().<String> consumer("Delete.Integer").handler(this::deleteInt);
        this.vertx.eventBus().<String> consumer("Update.Integer").handler(this::updateInt);
        this.vertx.eventBus().<String> consumer("Print.Integer").handler(this::print);
        this.vertx.eventBus().<String> consumer("Size.Integer").handler(this::size);
    }

    public final void size(final Message<String> m) {
        final MyTreeSet<Integer> tree = this.intMap.get(Integer.parseInt(m.body()));
        m.reply(m.body() + " anahtarli agacin boyutu " + tree.size());
    }

    public final void print(final Message<String> m) {
        final MyTreeSet<Integer> tree = this.intMap.get(Integer.parseInt(m.body()));
        m.reply(m.body() + " anahtarli agacin degerleri: " + tree.toString());
    }

    public final void updateInt(final Message<String> m) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            MyTreeSet<Integer> tree = this.intMap.get(studentTree.getTreeId());
            if (tree == null) {
                tree = new MyTreeSetImpl<Integer>("inorder", this.comparator);
                this.intMap.put(studentTree.getTreeId(), tree);
            }
            tree.updateValue(studentTree.getValue(), studentTree.getNewValue());
            m.reply(studentTree.getTreeId() + " anahtarli agacin degerleri: " + tree.toString());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public final void deleteInt(final Message<String> m) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            MyTreeSet<Integer> tree = this.intMap.get(studentTree.getTreeId());
            if (tree == null) {
                tree = new MyTreeSetImpl<Integer>("inorder", this.comparator);
                this.intMap.put(studentTree.getTreeId(), tree);
            }
            tree.remove(studentTree.getValue());
            m.reply(studentTree.getTreeId() + " anahtarli agacin degerleri: " + tree.toString());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public final void addInt(final Message<String> m) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            MyTreeSet<Integer> tree = this.intMap.get(studentTree.getTreeId());
            if (tree == null) {
                tree = new MyTreeSetImpl<Integer>("inorder", this.comparator);
                this.intMap.put(studentTree.getTreeId(), tree);
            }
            tree.add(studentTree.getValue());
            m.reply(studentTree.getTreeId() + " anahtarli agacin degerleri: " + tree.toString());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public final void createInt(final Message<String> m) {

        final ObjectMapper mapper = new ObjectMapper();
        try {
            final TreeRequest studentTree = mapper.readValue(m.body(), TreeRequest.class);
            final MyTreeSet<Integer> tree = new MyTreeSetImpl<Integer>(studentTree.getStrategy(), this.comparator);
            this.intMap.put(this.a, tree);
            m.reply(this.a + " anahtarli " + studentTree.getStrategy() + " Integer agac olusturuldu.");
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }
}
