package com.photoviewer.domain.interactor;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.photoviewer.data.repository.PhotoEntityRepository;
import com.photoviewer.domain.Photo;
import com.photoviewer.domain.mapper.photo.PhotoEntityToPhoto;
import com.photoviewer.presentation.di.modules.ApplicationModule;

import java.util.List;

import lombok.Setter;
import lombok.experimental.Accessors;
import roboguice.inject.ContextSingleton;
import rx.Observable;
import rx.Scheduler;

@ContextSingleton
@Accessors(prefix = "m")
public class SearchByTitle extends UseCase<List<Photo>> {

    @Setter
    private String mSearchedTitle;
    private PhotoEntityRepository mPhotoEntityRepository;
    private final PhotoEntityToPhoto photoTransformer;

    @Inject
    public SearchByTitle(@Named(ApplicationModule.BINDING_NAMED_SCHEDULER_COMPUTATION) Scheduler executionScheduler,
                         @Named(ApplicationModule.BINDING_NAMED_SCHEDULER_MAIN_THREAD) Scheduler observingScheduler,
                         PhotoEntityRepository photoEntityRepository) {
        super(executionScheduler, observingScheduler);
        mPhotoEntityRepository = photoEntityRepository;
        photoTransformer = new PhotoEntityToPhoto();
    }

    @Override
    protected Observable<List<Photo>> buildObservable() {
        return this.mPhotoEntityRepository.searchPhotosByTitle(mSearchedTitle).map(photoTransformer::transform);
    }
}

