package ru.softplat.stats.server.configuration;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageRequestOverride extends PageRequest {

    private final int from;

    protected PageRequestOverride(int from, int size, Sort sort) {
        super(from / size, size, sort);
        this.from = from;
    }

    public static PageRequestOverride of(int from, int size) {
        return new PageRequestOverride(from, size, Sort.unsorted());
    }

    @Override
    public long getOffset() {
        return from;
    }
}