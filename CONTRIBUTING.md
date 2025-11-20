# Contributing to Scenario Weaver

Thank you for your interest in contributing to Scenario Weaver!  
We welcome all contributions—bug reports, feature requests, code, and documentation.

---

## How to Contribute

### 1. Fork the Repository

Click “Fork” at the top right of the [GitHub repo](https://github.com/<your-org>/<your-repo>) and clone your fork locally.

```sh
git clone https://github.com/<your-username>/<your-repo>.git
```

### 2. Create a Branch

Create a new branch for your feature or fix.  
**Use a prefix to indicate the type of change:**

- `feat/` for new features (e.g., `feat/add-login`)
- `fix/` for bug fixes (e.g., `fix/null-pointer`)
- `docs/` for documentation (e.g., `docs/getting-started`)
- `chore/` for build or maintenance tasks (e.g., `chore/update-deps`)
- `test/` for tests (e.g., `test/add-integration-tests`)

```sh
git checkout -b feat/my-new-feature
```

### 3. Make Your Changes

- Follow the project’s code style.
- Add or update tests as needed.
- Update documentation if your change affects usage.

### 4. Test Your Changes

Run all tests to ensure nothing is broken:

```sh
mvn clean verify
```

### 5. Commit and Push

**Use [Conventional Commits](https://www.conventionalcommits.org/) for your commit messages:**

```
<type>(optional scope): short description

[optional body]

[optional footer(s)]
```

**Examples:**
- `feat(core): add scenario merging`
- `fix(junit): handle null payloads`
- `docs: update installation guide`
- `chore: update dependencies`

Commit and push:

```sh
git add .
git commit -m "feat(core): add scenario merging"
git push origin feat/my-new-feature
```

### 6. Open a Pull Request

Go to the original repo and open a Pull Request from your branch.  
Describe your changes and reference any related issues.

---

## Branching Model

- **`main`**: Production branch. Only stable, released code is merged here.
- **`development`**: Unstable branch. All new features and fixes should be based on and merged into `development` first.
- Feature/fix branches should be created from `development` and merged back into it via Pull Requests.

---

## Code of Conduct

Please be respectful and follow our [Code of Conduct](CODE_OF_CONDUCT.md).

---

## Reporting Issues

- Use [GitHub Issues](https://github.com/<your-org>/<your-repo>/issues) for bugs and feature requests.
- Provide as much detail as possible.

---

## Need Help?

Open an issue or start a discussion—maintainers and the community are here to help!

---

Thank you for helping make Scenario Weaver better!