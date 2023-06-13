import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userId: string = '';

  constructor(private httpClient: HttpClient) {
  }

  subscribeToUser(userId: string): Observable<boolean> {
    return this.httpClient.post<boolean>("http://localhost:8080/api/user/subscribe/" + userId, null);
  }

  unSubscribeUser(userId: string): Observable<boolean> {
    return this.httpClient.post<boolean>("http://localhost:8080/api/user/unSubscribe/" + userId, null);
  }

  registerUser() {
    this.httpClient.get("http://localhost:8080/api/user/register", {responseType: "text"})
      .subscribe(data => {
        this.userId = data; //so this way we can get the userId and save it to the var, and to access the var if we directly use it then it will violates the emcapsulation princeiplek, so save it inside the method getuserid() to avoid it.
      })
  }

  getUserId(): string {
    return this.userId;
  }
}
