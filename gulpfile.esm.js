import { dest, parallel, series, src, watch } from 'gulp';
import del from 'del';
import livereload from 'gulp-livereload';
import sass from 'gulp-sass';
import minifycss from 'gulp-minify-css';
import concat from 'gulp-concat';
import autoprefixer from 'gulp-autoprefixer';
import yargs from 'yargs';
import gulpif from 'gulp-if';

const production = !!yargs.argv.production;

const dirs = {
  src: 'src',
  dest: 'build',
};

const sources = {
  styles: `${dirs.src}/**/*.scss`,
};

const files = {
  css: `styles.min.css`,
};

export const buildStyles = () => src(sources.styles)
  .pipe(sass.sync().on('error', sass.logError))
  .pipe(gulpif(production, minifycss()))
  .pipe(autoprefixer('last 2 version', 'safari 5', 'ie 8', 'ie 9'))
  .pipe(concat(files.css))
  .pipe(gulpif(production, dest(dirs.dest), dest(dirs.src)))
  .pipe(livereload());

export const clean = () => del(['build']);

export const devWatch = () => {
  livereload.listen();
  watch(sources.styles, buildStyles);
};

export const dev = series(clean, parallel(buildStyles), devWatch);

export const build = series(clean, parallel(buildStyles));
