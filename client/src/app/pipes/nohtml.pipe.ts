import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'nohtml'})
export class NoHtmlPipe implements PipeTransform {
  transform(value: string): string {
    return value.replace(/<(.|\n)*?>/g, '').replace(/&nbsp;/gi, ' ');
  }
}