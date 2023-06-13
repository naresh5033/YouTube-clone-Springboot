import {Component, OnInit} from '@angular/core';
import {VideoService} from "../video.service";
import {VideoDto} from "../video-dto";

@Component({
  selector: 'app-featured',
  templateUrl: './featured.component.html',
  styleUrls: ['./featured.component.css']
})
export class FeaturedComponent implements OnInit {

  featuredVideos: Array<VideoDto> = [];

  constructor(private videoService: VideoService) {
  }

  ngOnInit(): void {
    this.videoService.getAllVideos().subscribe(response => { // since the getAllVideos() is observable
      this.featuredVideos = response; // now our featuredVideos[] will contains all the info from our backend
    })
  }

}
