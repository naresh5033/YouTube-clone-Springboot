import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatChipInputEvent } from '@angular/material/chips';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { ActivatedRoute } from '@angular/router';
import { VideoService } from '../video.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { VideoDto } from '../video-dto';

@Component({
  selector: 'app-save-video-details',
  templateUrl: './save-video-details.component.html',
  styleUrls: ['./save-video-details.component.css'],
})
export class SaveVideoDetailsComponent {
  saveVideoDetailsForm: FormGroup;
  title: FormControl = new FormControl('');
  description: FormControl = new FormControl('');
  videoStatus: FormControl = new FormControl('');
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  tags: string[] = [];
  selectedFile!: File;
  selectedFileName = '';
  videoId = '';
  fileSelected = false;
  videoUrl!: string;
  thumbnailUrl!: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private videoService: VideoService,
    private matSnackBar: MatSnackBar
  ) {
    this.videoId = this.activatedRoute.snapshot.params.videoId; //the id of the video in the param /:id, since we need the id to corelate to the thumbnail.
    this.videoService.getVideo(this.videoId).subscribe((data) => {
      this.videoUrl = data.videoUrl;
      this.thumbnailUrl = data.thumbnailUrl;
    }); // to get the vidio and thumbnail url
    this.saveVideoDetailsForm = new FormGroup({
      //our form consist of 3 fields
      title: this.title,
      description: this.description,
      videoStatus: this.videoStatus,
    });
  }

  // this code was ref from the ang material/chips library -- for our tags (video)
  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our fruit
    if (value) {
      this.tags.push(value);
    }

    // Clear the input value
    event.chipInput!.clear();
  }

  remove(value: string): void {
    const index = this.tags.indexOf(value);

    if (index >= 0) {
      this.tags.splice(index, 1);
    }
  }

  // retrieve the file that user has selected
  onFileSelected(event: Event) {
    //from the form element
    // @ts-ignore
    this.selectedFile = event.target.files[0];
    this.selectedFileName = this.selectedFile.name; //get the name from the file that we selected
    this.fileSelected = true; //we can make a condn in html with this var, once the file is selected to upload, then only enable the upload button(and the selected file name/info)
  }

  // upload the file to the server
  onUpload() {
    this.videoService
      .uploadThumbnail(this.selectedFile, this.videoId)
      .subscribe(() => {
        //since the upload service is observable, so ve can subscribe to it
        this.matSnackBar.open('Thumbnail Upload Successful', 'OK');
      });
  }

  saveVideo() {
    //make a call to backend (video service)
    const videoMetaData: VideoDto = {
      id: this.videoId,
      title: this.saveVideoDetailsForm.get('title')?.value,
      description: this.saveVideoDetailsForm.get('description')?.value,
      tags: this.tags,
      videoStatus: this.saveVideoDetailsForm.get('videoStatus')?.value,
      videoUrl: this.videoUrl,
      thumbnailUrl: this.thumbnailUrl,
      likeCount: 0,
      dislikeCount: 0,
      viewCount: 0,
    };
    this.videoService.saveVideo(videoMetaData).subscribe((data) => {
      this.matSnackBar.open('Video Metadata Updated successfully', 'OK');
    });
  }
}
