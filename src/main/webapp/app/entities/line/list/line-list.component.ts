import { Component } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { LineService } from "../line.service";
import { ILine } from "../line.model";
import { Location } from "@angular/common";
import { Router } from "@angular/router";
import { IUploadFileLine } from "../../../model/upload-file-line.model";
import { UploadFileService } from "../../../services/upload-file.service";
import { ShiftLineService } from "../../../services/shift-line.service";

@Component({
    selector: 'app-line-list',
    standalone: true,
    imports: [
        SharedModule,
    ],
    templateUrl: './line-list.component.html',
    styleUrls: ['./line-list.component.scss'],
})
export class LineListComponent {

    lineList: ILine[] = [];
    // file import
    showImportPopup: boolean = false;
    selectedFile: File | null = null;
    uploadProgress: number = 0;
    uploading: boolean = false;
    uploadSuccess: boolean = false;
    uploadError: string | null = null;
    resultFileUpload!: IUploadFileLine;

    constructor(
        private lineService: LineService,
        private location: Location,
        private shiftLineService: ShiftLineService,
        private router: Router,
        private uploadFileService: UploadFileService,
    ) {}

    ngOnInit(): void {
        this.lineService.getAll().subscribe( res => {
            this.lineList = res;
        });
    }

    delete(line: ILine): void {
        if(confirm('Are you sure you want to delete?')) {
            this.lineService.delete(line.id!).subscribe({
            next: () => {this.reloadPage(1000)},
            error: (err) => {console.log('Error delete ', err)}
        });
        }
    }

    selectLineForShift(line: ILine) {
        this.shiftLineService.setLine(line);
        this.router.navigate(['/shifts/new']);
    }

    openImportPopup() {
        this.showImportPopup = true;
        this.selectedFile = null;
        this.uploadProgress = 0;
        this.uploading = false;
        this.uploadSuccess = false;
        this.uploadError = null;
    }

    closeImportPopup() {
        this.showImportPopup = false;
        this.selectedFile = null;
        this.uploadProgress = 0;
        this.uploading = false;
        this.uploadSuccess = false;
        this.uploadError = null;
    }

    triggerFileInput() {
        document.getElementById('fileInput')?.click();
    }

    onFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;
        if(input.files && input.files.length > 0) {
            const file = input.files[0];
            if(file.name.endsWith('.xlsx')) {
                this.selectedFile = file;
                this.uploadError = null;
            } else {
                this.selectedFile = null;
                this.uploadError = 'Invalid file type. Please select an .xlsx file.';
                input.value = '';
            }
        } else {
            this.selectedFile = null;
            this.uploadError = null;
        }
    }

    uploadFile() {
        if(!this.selectedFile) {
            this.uploadError = 'Please select a file first.';
            return;
        }
        this.uploading = true;
        this.uploadSuccess = false;
        this.uploadError = null;
        this.uploadProgress = 0;

        const formData = new FormData();
        formData.append('file', this.selectedFile, this.selectedFile.name);

        this.uploadFileService.uploadLineFile(formData).subscribe({
            next: (res) => { 
                this.resultFileUpload = res;
                this.uploading = false;
                this.uploadSuccess = true;
                this.reloadPage(3000);
            }, 
            error: (err) => {
                this.uploading = false;
                this.uploadSuccess = false;
                this.uploadError = 'Upload failed: ' + (err.error?.message || err.message || 'Unknown error');
                console.error('Upload Error:', err);
            }
        });
    }

    reloadPage(time: number) {
        if(time>0) {
            setTimeout(() => {
                window.location.reload();
            }, time);
        } 
    }
 
}