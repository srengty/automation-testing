import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import expressListRoutes from 'express-list-routes';
import { ConsoleLogger } from '@nestjs/common';

async function bootstrap() {
  const app = await NestFactory.create(AppModule,{
    logger: new ConsoleLogger()
  });
  await app.init();
  console.log('Available routes:');
  expressListRoutes(app.getHttpAdapter().getInstance());
  await app.listen(process.env.PORT ?? 3000);
}
bootstrap();
