import { LoginDto } from "./login-dto.model";

export interface RegisterDto extends LoginDto {
    fullName: string;
}