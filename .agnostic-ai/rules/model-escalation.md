# Adaptive Model Selection

Selects between a **standard model** (fast, low-cost, routine tasks) and a **frontier model** (powerful, complex reasoning/large context) to balance cost, speed, and capability. Default to standard; escalate only when necessary.

## Rule Configuration

```yaml
name: adaptive-model-selection
version: 1.0

defaults:
  model_tier: standard
routing:
  escalate_to_frontier_if:
    - condition: task.complexity == "high"
      examples:
        - multi-step reasoning
        - complex algorithms
        - architectural decisions
        - lexer/parser grammar changes with ambiguity
    - condition: task.scope == "large"
      examples:
        - multi-file changes
        - cross-package refactors
        - long context inputs
    - condition: task.ambiguity == "high"
      examples:
        - unclear requirements
        - conflicting instructions
    - condition: task.type == "hard_debugging"
      examples:
        - PSI / parser edge cases
        - intermittent test failures
        - completion ordering / prefix-matching issues
    - condition: execution.failed == true
    - condition: confidence < 0.8 AND task.complexity != "low"
    - condition: task.requires_planning == true
      examples:
        - multi-step tool usage
        - agent workflows
  fallback_to_standard_if:
    - condition: task.remaining_work == "routine"
    - condition: task.phase == "implementation"
    - condition: confidence >= 0.9 AND no_errors == true
strategy:
  initial_attempt:
    model: standard
  retry_on_failure:
    max_attempts: 1
    then: frontier
  early_escalation:
    enabled: true
constraints:
  - avoid_unnecessary_escalation: true
  - minimize_model_switching: true
  - prioritize_cost_efficiency: true
observability:
  emit_decision_log: true
  log_fields:
    - task_complexity
    - task_scope
    - ambiguity
    - confidence
    - selected_model_tier
```

## Notes

- Start standard; escalate to frontier for complex/ambiguous/large/failing or reasoning-heavy/planning-heavy work; return to standard for routine implementation
- Do not escalate for simple, well-defined tasks (registry entries, single-completion additions, doc fixes)
- Prefer early escalation over repeated failure loops
- Maintain consistent output regardless of model tier
