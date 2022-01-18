package com.foreks.feed;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        this.vertx.deployVerticle("com.foreks.feed.StudentVerticle");
        this.vertx.deployVerticle("com.foreks.feed.IntVerticle");
        this.vertx.deployVerticle("com.foreks.feed.StringVerticle");
        final Router router = Router.router(this.vertx);
        // Tree Created
        router.post().handler(r -> {
            final String type = r.request().getHeader("dataType");
            r.request().handler(b -> {
                final String bodyAsString = b.getString(0, b.length(), "UTF-8");
                this.vertx.eventBus().send("Create." + type, bodyAsString, reply -> r.response().end((String) reply.result().body()));
            });
        });

        // Add Element
        router.put().handler(r -> {
            final String type = r.request().getHeader("dataType");
            r.request().bodyHandler(b -> {
                final String body = b.getString(0, b.length(), "UTF-8");
                this.vertx.eventBus().send("Add." + type, body, reply -> r.response().end((String) reply.result().body()));
            });
        });
        // Delete Element
        router.delete().handler(r -> {
            final String type = r.request().getHeader("dataType");
            r.request().bodyHandler(b -> {
                final String body = b.getString(0, b.length(), "UTF-8");
                this.vertx.eventBus().send("Delete." + type, body, reply -> r.response().end((String) reply.result().body()));
            });
        });
        // Update Value
        router.patch().handler(r -> {
            final String type = r.request().getHeader("dataType");
            r.request().bodyHandler(b -> {
                final String body = b.getString(0, b.length(), "UTF-8");
                this.vertx.eventBus().send("Update." + type, body, reply -> r.response().end((String) reply.result().body()));
            });
        });
        // Print Value
        router.get("/print").handler(r -> {
            final String type = r.request().getHeader("dataType");
            final String treeId = r.request().getHeader("treeId");
            this.vertx.eventBus().send("Print." + type, treeId, reply -> r.response().end((String) reply.result().body()));
        });
        router.get("/size").handler(r -> {
            final String type = r.request().getHeader("dataType");
            final String treeId = r.request().getHeader("treeId");
            this.vertx.eventBus().send("Size." + type, treeId, reply -> r.response().end((String) reply.result().body()));
        });

        this.vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    }
}
