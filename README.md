bencode-parser
==============

Fast and efficient bencode( torrent file format ) pull parser.
Very small, no additional dependencies.

It does not create object graph in memory, just a tiny index over data.

Parser currently is not thread-safe.

Usage:

See tests

Tips:

If you are trying to decode bencoded file, use Buffered Readers - it will increase perfomance
