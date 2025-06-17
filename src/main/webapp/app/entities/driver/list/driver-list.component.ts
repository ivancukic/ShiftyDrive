import { Component, OnInit } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { IDriver } from "../driver.model";
import { DriverService } from "../driver.service";
import { Location } from "@angular/common";
import { IUploadFileDriver } from "../../../model/upload-file-driver.model";
import { UploadFileService } from "../../../services/upload-file.service";
import { FormsModule } from "@angular/forms";
import { ICategory } from "../../category/category.model";
import { CategoryService } from "../../category/category.service";

@Component({
    selector: 'app-driver-list',
    standalone: true,
    imports: [
        SharedModule,
        FormsModule,
    ],
    templateUrl: './driver-list.component.html',
    styleUrls: ['./driver-list.component.scss'],
})
export class DriverListComponent implements OnInit {

    driverList: IDriver[] = [];
    categoryList: ICategory[] = [];
    // file import
    showImportPopup: boolean = false;
    selectedFile: File | null = null;
    uploadProgress: number = 0;
    uploading: boolean = false;
    uploadSuccess: boolean = false;
    uploadError: string | null = null;
    resultFileUpload!: IUploadFileDriver;

    // Filter date
    dobFilterType: 'none' | 'before' | 'after' | 'between' = 'none';
    
    filters = {
        name: '',
        dobBefore: '',
        dobAfter: '',
        dobBetweenStart: '',
        dobBetweenEnd: '',
        active: undefined as boolean | undefined,
        category: null as ICategory | null,
    };

    constructor(
        private driverService: DriverService,
        private location: Location,
        private uploadFileService: UploadFileService,
        private categoryService: CategoryService,
    ) {}

    ngOnInit(): void {
        this.driverService.getAll().subscribe( res => {
            this.driverList = res;
        });
        this.categoryService.getAll().subscribe( res => {
            this.categoryList = res;
        });
    }

    delete(driver: IDriver): void {
        if(confirm('Are you sure you want to delete?')) {
            this.driverService.delete(driver.id!).subscribe({
            next: () => {this.reloadPage(0)},
            error: (err) => {console.log('Error delete ', err)}
        });
        }
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

        this.uploadFileService.uploadDriverFile(formData).subscribe({
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

    inputFilter(filter: any, inputField: string) {
        if(inputField==='all') {
            this.driverService.getAll().subscribe( res => {
                this.driverList = res;
            });
        }
        if(inputField==='name') {
            this.filters.name = filter.value;
        }
        if(inputField==='before') {
            this.filters.dobBefore = filter.value;
        }
        if(inputField==='after') {
            this.filters.dobAfter = filter.value;
        }
        if(inputField==='betweenStart') {
            this.filters.dobBetweenStart = filter.value;
        }
        if(inputField==='betweenEnd') {
            this.filters.dobBetweenEnd = filter.value;
        }
        if (inputField === 'active-filter') {
            if (filter.value === 'all') {
                this.filters.active = undefined;
            } else {
                this.filters.active = filter.value === 'true';
            }
        }
        if (inputField === 'category-filter') {
            const selectedId = +filter.value;
            if (filter.value === 'all') {
                this.filters.category = null;
            } else {
                this.filters.category = this.categoryList.find(c => c.id === selectedId) || null;
            }
        }
        this.driverService.filterDrivers(
            this.filters.name,
            this.filters.dobBefore,
            this.filters.dobAfter,
            this.filters.dobBetweenStart,
            this.filters.dobBetweenEnd,
            this.filters.active,
            this.filters.category?.id
        ).subscribe({
            next: (res) => { this.driverList = res },
            error: (err) => { console.log('Filter error', err) }
        });
    }

    // Filter date of birth reset
    resetDobFilters(): void {
        this.filters = {
            name: '',
            dobBefore: '',
            dobAfter: '',
            dobBetweenStart: '',
            dobBetweenEnd: '',
            active: undefined as boolean | undefined,
            category: null as ICategory | null,
        };
        this.inputFilter('', 'all');
    }

    applyDobFilter(filter: any, inputField: string): void {
        this.inputFilter(filter, inputField);
    }

}