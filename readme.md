# node-xslt-java-bridge

Node.js module exposes basic XSLT API
through [Java bridge](https://github.com/joeferner/node-java)
to [The Saxon XSLT and XQuery Processor from Saxonica Limited](http://www.saxonica.com/).

Includes [Saxon-HE](http://www.saxonica.com/download/opensource.xml) distribution.
All notices can be found in `bridge/vendor/SaxonHE9-7-0-14J/notices/` directory.

License: [MPL 2.0](https://www.mozilla.org/en-US/MPL/2.0/)

## Installation
```
npm install node-xslt-java-bridge -S
```

## Usage
```js
var Transformer = require('node-xslt-java-bridge').Transformer;

var transformer = new Transformer('template.xsl');
var results = transformer.transform({
    base: '/absolute/path/to/working/dir',
    path: 'path/to/document.xml',
    contents: '<xml>...</xml>'
});
console.log(results[0].contents);
```

Also see `examples/` in the source repository.

### Troubleshooting: Popup dialog to install legacy JAVA SE 6 on macOS
It could be because the Oracle JDK does not advertise itself as available for JNI.
Please see [this issue](https://github.com/joeferner/node-java/issues/90#issuecomment-45613235)
for more details and manual workarounds.


## API

Please refer to `index.d.ts` file.

## Gulp Plugin
There is also a plugin for [Gulp](http://gulpjs.com/) based on this module — [`gulp-xslt2`](https://github.com/nocheinarbeiter/gulp-xslt2).
