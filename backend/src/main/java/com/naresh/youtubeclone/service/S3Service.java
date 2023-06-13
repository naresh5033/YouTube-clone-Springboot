package com.naresh.youtubeclone.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service implements FileService {


    public static
    final String BUCKET_NAME = "naresh-xo-docs";
    private final AmazonS3Client awsS3Client;

    @Override
    public String uploadFile(MultipartFile file) { //lets retrive the extn of the file
        var filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
//now lets gen the uuid and append it to the extn of the file
        var key = UUID.randomUUID().toString() + "." + filenameExtension;

        var metadata = new ObjectMetadata(); // from s3 module
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

//        now we ve the metadata lets send it to s3 by using the s3 client
        try {
            awsS3Client.putObject(BUCKET_NAME, key, file.getInputStream(), metadata);
        } catch (IOException ioException) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An Exception occurred while uploading the file");
        }
//        lets add the access control list to make the video file public
        awsS3Client.setObjectAcl(BUCKET_NAME, key, CannedAccessControlList.PublicRead);

        //now retrieve the video url
        return awsS3Client.getResourceUrl(BUCKET_NAME, key);
    } //lets call this upload s3 service in our video service
}
