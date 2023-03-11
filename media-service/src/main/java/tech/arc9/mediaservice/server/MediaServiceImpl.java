package tech.arc9.mediaservice.server;


import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.arc9.mediaservice.MediaServiceGrpc;
import tech.arc9.mediaservice.MediaServiceProto;
import tech.arc9.mediaservice.manager.MediaManager;


@Service
public class MediaServiceImpl extends MediaServiceGrpc.MediaServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(MediaServiceImpl.class);

    @Autowired private MediaManager mediaManager;


    @Override
    public void getDpUploadUrl(MediaServiceProto.Request request, StreamObserver<MediaServiceProto.Response> responseObserver) {
        responseObserver.onNext(mediaManager.getDpUploadUrl(request));
        responseObserver.onCompleted();
    }

    @Override
    public void getDpDownloadUrl(MediaServiceProto.Request request, StreamObserver<MediaServiceProto.Response> responseObserver) {
        responseObserver.onNext(mediaManager.getDpDownloadUrl(request));
        responseObserver.onCompleted();
    }


}
