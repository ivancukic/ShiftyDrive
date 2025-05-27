import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";

// Import PrimeNG modules
// import { ButtonModule } from 'primeng/button';

@NgModule({
    imports:[
        // ButtonModule,
    ],
    exports: [
        CommonModule,
        RouterModule,
        // ButtonModule,
    ],
    providers:[]
})
export default class SharedModule {}