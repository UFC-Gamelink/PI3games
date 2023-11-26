package com.gamelink.gamelinkapi.utils.creators;

import com.gamelink.gamelinkapi.dtos.requests.communities.CommunityRequest;

public class CommunityRequestCreator implements Creator<CommunityRequest>{
    private static CommunityRequestCreator INSTANCE;

    private CommunityRequestCreator() {}

    public static CommunityRequestCreator getInstance() {
        if (INSTANCE == null) INSTANCE = new CommunityRequestCreator();
        return INSTANCE;
    }
    @Override
    public CommunityRequest createValid() {
        return null;
    }
}
