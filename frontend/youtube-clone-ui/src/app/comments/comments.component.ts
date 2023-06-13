import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {UserService} from "../user.service";
import {CommentsService} from "../comments.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CommentDto} from "../comment-dto";

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {

  @Input()
  videoId: string = ''; //as we re recieving this video id as i/p from the video detail comp's html 
  commentsForm: FormGroup;
  commentsDto: CommentDto[] = [];

  constructor(private userService: UserService, private commentService: CommentsService,
              private matSnackBar: MatSnackBar) { 
    this.commentsForm = new FormGroup({
      comment: new FormControl('comment'),
    });
  }

  ngOnInit(): void {
    this.getComments();
  }

  postComment() {
    const comment = this.commentsForm.get('comment')?.value; // get whatever val the user type inside the comment (text) field

    //after getting the comment, we ve to prepare the req body(comment Dto) and make a call to backend 
    const commentDto = {
      "commentText": comment,
      "authorId": this.userService.getUserId()
    }

    this.commentService.postComment(commentDto, this.videoId).subscribe(() => {
      this.matSnackBar.open("Comment Posted Successfully", "OK");

      //now once we posted the comment we ve to clear/reset the form fields so we can post a new comment.
      this.commentsForm.get('comment')?.reset();

      //now we can display the posted comments
      this.getComments();
    })
  }

  getComments() {
    this.commentService.getAllComments(this.videoId).subscribe(data => {
      this.commentsDto = data;
    });
  }
}
