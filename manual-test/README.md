# Manual test project

A small Phel project to open in the plugin sandbox, so the features that are awkward to assert
headlessly can be exercised by hand.

```bash
./gradlew runIde --args="$PWD/manual-test"
```

The `--args` passes the project path straight through to the IDE, so the sandbox opens here rather
than on the welcome screen.

## What it covers

`src/refactoring.phel` is annotated per form with what to select and what to expect. It is arranged
so each refactoring has both an interesting case and a case that must be *declined*:

| Feature | Interesting case | Must decline / must not |
|---|---|---|
| Extract Variable | appends to an existing `let` rather than nesting one | wraps the body when there is no `let` |
| Extract Function | derives the parameter list from free variables | `str` is stdlib, must not become a parameter |
| Safe Delete | removes the whole form, not just the name | refuses while a usage remains; a recursive call is not a usage |
| Parameter info | `Ctrl+P` inside a call | — |
| Smart enter | Complete Current Statement closes open brackets | — |
| Optimize Imports | drops the unused `json` require | keeps the used `str` one |
| TODO indexing | the `TODO` comment reaches the TODO tool window | — |

## Why it exists

`./gradlew runIde` is the documented manual-test path, but there was nothing to open in it — so
every manual check started by hand-writing a file first. The transformations themselves are covered
by the integration suite; this is for the parts that need eyes on the IDE: menu placement, popup
rendering, gutter icons and the file-type icon.

It is not built, tested or shipped. `phel-config.php` is here only so the layout looks like a real
Phel project to the plugin.
