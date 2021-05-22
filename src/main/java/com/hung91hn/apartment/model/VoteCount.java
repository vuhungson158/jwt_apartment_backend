package com.hung91hn.apartment.model;

import java.util.List;

public class VoteCount {
    public long positive, negative;

    public VoteCount(List<Vote> votes) {
        positive = count(votes, true);
        negative = count(votes, false);
    }

    private long count(List<Vote> votes, boolean positive) {
        return votes.stream().filter(v -> positive == v.positive).count();
    }
}
