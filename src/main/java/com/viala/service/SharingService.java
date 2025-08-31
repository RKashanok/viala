package com.viala.service;

import com.viala.model.Sharing;
import com.viala.repository.SharingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SharingService {

    private final SharingRepository sharingRepository;

    @Autowired
    public SharingService(SharingRepository sharingRepository) {
        this.sharingRepository = sharingRepository;
    }

    public Sharing saveSharing(Sharing sharing) {
        return sharingRepository.save(sharing);
    }
}
