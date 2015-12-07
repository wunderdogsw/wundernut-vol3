'use strict';

const
  fs = require('fs'),
  gulp = require('gulp'),
  less = require('gulp-less'),
  babel = require('gulp-babel'),
  serve = require('gulp-serve'),
  autoprefixer = require('gulp-autoprefixer'),
  calculate = require('./gulp/calculate.js');

 
gulp.task('default', ['css', 'js', 'copy', 'watch', 'calculate', 'serve']);


gulp.task('calculate', () => {

  let
    dir = './dist',
    file = dir + '/combinations.json',
    combinations = calculate();

  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir);
  }

  fs.writeFile(file, JSON.stringify(combinations), e => {
    if (e) {
      console.log(e);
      return;
    }
    console.log('Saved results into ' + file);
  });

});


gulp.task('watch', () => {
  gulp.watch(['src/css/**/*.less'], ['css']);
  gulp.watch(['src/js/**/*.js'], ['js']);
  gulp.watch(['src/**/*.*', '!src/css/**/*.less', '!src/js/**/*.js'], ['copy']);
  gulp.watch(['gulp/*.js'], ['calculate']);
});


gulp.task('css', () => {
  gulp
    .src('src/css/**/*.less')
    .pipe(less())
    .pipe(autoprefixer({
      browsers: ['last 3 versions'],
      cascade: false
    }))
    .pipe(gulp.dest('dist/css'));
});


gulp.task('js', () => {
  gulp
    .src('src/js/**/*.js')
    .pipe(babel())
    .pipe(gulp.dest('dist/js'));
});


gulp.task('copy', () => {
  gulp
    .src(['src/**/*.*', '!src/css/**/*.less', '!src/js/**/*.js'])
    .pipe(gulp.dest('dist'));
});


gulp.task('serve', serve('dist'));
