bencode-parser
==============

Fast and efficient bencode( torrent file format ) stream parser.
Very small, no additional dependencies.

It doesn`t create object graph in memory, just create tiny index for underlying data.

Parser currently is not thread-safe.

Usage:

See tests

Tips:

If you are trying to decode bencoded file, use Buffered Readers - it will increase perfomance
