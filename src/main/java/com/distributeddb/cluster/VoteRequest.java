package com.distributeddb.cluster;

public class VoteRequest {

    private final int candidateId;
    private final int term;

    public VoteRequest(
            int candidateId,
            int term) {

        this.candidateId = candidateId;
        this.term = term;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public int getTerm() {
        return term;
    }
}