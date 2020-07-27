package com.jiangkang.storage.greendao;

/**
 * Created by jiangkang on 2017/11/13.
 * description：
 */

public class Note {

    private Long id;


    private String content;


    private String timeAdded;


    public Note(Long id, String content, String timeAdded) {
        this.id = id;
        this.content = content;
        this.timeAdded = timeAdded;
    }

    public Note() {
    }


    public String getContent() {
        return content;
    }

    public Note setContent(String content) {
        this.content = content;
        return this;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public Note setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
