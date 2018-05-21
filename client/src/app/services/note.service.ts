/**
 * Created by Shvets on 04.05.2018.
 */

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Note} from "../model/note";

@Injectable()
export class NoteService {

  private base_url = '/note/';


  constructor(private http: HttpClient) {

  }

  public createNote(note:Note) {
    console.log('note.service.createNote: ' + note);
    return this.http.post(this.base_url, note);

  }

  public getFolderNotes(folderId: number) {
    return this.http.get(this.base_url + 'folder/' + folderId);
  }

  public getNoteById(noteId: number) {
    return this.http.get(this.base_url + noteId);
  }

}
