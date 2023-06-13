import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-video-player',
  templateUrl: './video-player.component.html',
  styleUrls: ['./video-player.component.css']
})
export class VideoPlayerComponent {

  @Input()
  videoUrl!: string | ''; //our player could play the video of the specified video url(the one that user uploaded)

}
