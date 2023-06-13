import {Component} from '@angular/core';
import {FileSystemDirectoryEntry, FileSystemFileEntry, NgxFileDropEntry} from 'ngx-file-drop';
import {VideoService} from "../video.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-upload-video',
  templateUrl: './upload-video.component.html',
  styleUrls: ['./upload-video.component.css']
})

// lets enable the drag and drop
export class UploadVideoComponent {

// the code we can grab from the ngx-file-drop docs
  public files: NgxFileDropEntry[] = [];
  fileUploaded: boolean = false;
  fileEntry: FileSystemFileEntry | undefined;

// inject our video service class and the router
  constructor(private videoService: VideoService, private router: Router) {
  }

  public dropped(files: NgxFileDropEntry[]) {
    this.files = files;
    for (const droppedFile of files) {

      // Is it a file?
      if (droppedFile.fileEntry.isFile) {
        this.fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
        this.fileEntry.file((file: File) => {
          // Here you can access the real file
          console.log(droppedFile.relativePath, file);

          this.fileUploaded = true;
          /**
           // You could upload it like this:
           const formData = new FormData()
           formData.append('logo', file, relativePath)

           // Headers
           const headers = new HttpHeaders({
            'security-token': 'mytoken'
          })

           this.http.post('https://mybackend.com/api/upload/sanitize-and-save-logo', formData, { headers: headers, responseType: 'blob' })
           .subscribe(data => {
            // Sanitized logo returned from backend
          })
           **/

        });
      } else {
        // It was a directory (empty directories are added, otherwise only files)
        const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
        console.log(droppedFile.relativePath, fileEntry);
      }
    }
  }

  public fileOver(event: any) {
    console.log(event);
  }

  public fileLeave(event: any) {
    console.log(event);
  }

// lets make the file upload, from backend, and pull the video Service class
  uploadVideo() {
    if (this.fileEntry !== undefined) { //we upload only if the file entry is not undefined
      console.log(this.fileEntry);

      this.fileEntry.file(file => {
        this.videoService.uploadVideo(file).subscribe(data => {
          this.router.navigateByUrl("/save-video-details/" + data.videoId); //once the video is uploaded then nav to the save video details comp
        })
      })
    }
  }
}
