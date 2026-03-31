import { BadRequestException, Body, Controller, HttpCode, HttpStatus, Post, Response } from '@nestjs/common';
import { AuthService } from './auth.service';
import { response } from 'express';

@Controller('auth')
export class AuthController {
  constructor(private authService: AuthService) {}

  @HttpCode(HttpStatus.OK)
  @Post('login')
  signIn(@Body() signInDto: Record<string, any>, @Response() res: Response) {
    console.log('Received sign-in request:', signInDto);
    if(!signInDto.email || !signInDto.password) {
      console.warn('Missing email or password in sign-in request');
      throw new BadRequestException('Email and password are required'); // Throw an error if email or password is missing
      //res.status(HttpStatus.BAD_REQUEST);
      //return {message: 'Email and password are required'}; // Return an error message if email or password is missing
    }
    return this.authService.signIn(signInDto.email, signInDto.password);
  }
}
